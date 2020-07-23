package amber.sync.safeexec

import amber.collections.randomItem
import amber.collections.sync
import amber.coroutines.JobManger
import amber.coroutines.OptimalDispatcher
import amber.coroutines.scope
import amber.factory.Factory
import amber.factory.factory
import amber.sync.SyncMode
import amber.sync.Synchronized
import amber.sync.exec.SynchronisationScope
import amber.sync.exec.SynchronizedExecutable
import amber.sync.exec.SynchronizedExecutor
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class SafeSynchronizedExecutorManger(
        private val scopeTimeout: Long = 0L,
        private val synchronized: Synchronized = Synchronized(
                syncMode = SyncMode.UNSAFE_EXECUTOR, syncExecutor = SynchronizedExecutor(SynchronisationScope())
        ),
        private val synchronisationScopeFactory: Factory<SynchronisationScope> = factory { SynchronisationScope() },
        private val coroutineScope: CoroutineScope = OptimalDispatcher("SafeSynchronizedExecutorManger").scope()
) {

    private val scopes: MutableMap<SynchronisationScope, Channel<SynchronizedExecutable<*>>?> = mutableMapOf()

    private val jobManager = JobManger(mutableListOf<Job>().sync(synchronized))

    private fun bindExecutableChannelToAnyScope(channel: Channel<SynchronizedExecutable<*>>): SynchronisationScope {
        var scope: SynchronisationScope? = null
        synchronized.synchronized {
            if (scopes.none { it.value === channel }) {
                val emptyScopes = scopes.filter { it.value == null }
                scope = if (emptyScopes.isEmpty()) {
                    synchronisationScopeFactory()
                } else {
                    emptyScopes.entries.randomItem().key
                }
                scopes.put(scope!!, channel)
                return@synchronized
            } else {
                scope = scopes.filter { it.value === channel }.entries.firstOrNull()!!.key
            }
        }
        return scope!!
    }

    private fun unbindExecutableChannel(channel: Channel<SynchronizedExecutable<*>>) {
        synchronized.synchronized {
            val scope: SynchronisationScope? = scopes.filter { it.value === channel }.entries.firstOrNull()?.key
            if (scope != null) {
                scopes.put(scope, null)
            }
        }
    }

    fun createExecutor(): SafeSynchronizedExecutor {
        val executorChanel = Channel<SynchronizedExecutable<*>>()
        manage(executorChanel)
        return SafeSynchronizedExecutor(executorChanel, this)
    }

    private object KillMarker : SynchronizedExecutable<Unit>() {
        override fun run() {
            throw IllegalStateException()
        }
    }

    private fun manage(channel: Channel<SynchronizedExecutable<*>>) {
        with(coroutineScope) {
            with(jobManager) {
                val usedWithInTimeout = atomic(false)
                launch {
                    val scopeChannel = Channel<SynchronizedExecutable<*>>()
                    var lastWasKill = true
                    var lastJob: Job? = null
                    while (isActive && !channel.isClosedForReceive && !channel.isClosedForSend) {
                        val receiveResult = channel.receive()
                        usedWithInTimeout.getAndSet(true)
                        if (lastWasKill) {
                            lastWasKill = false
                            lastJob?.join()
                            bindExecutableChannelToAnyScope(channel).apply {
                                lastJob = launch {
                                    var closeScope = false
                                    while (!closeScope && isActive && !scopeChannel.isClosedForReceive && !scopeChannel.isClosedForSend) {
                                        val receiveResult = scopeChannel.receive()
                                        if (receiveResult === KillMarker) {
                                            closeScope = true
                                        } else {
                                            receiveResult.execute()
                                        }
                                    }
                                    unbindExecutableChannel(channel)
                                }
                            }
                            launch {
                                var closeScope = false
                                while (isActive && !closeScope) {
                                    delay(scopeTimeout)
                                    if (usedWithInTimeout.getAndSet(false)) {
                                        channel.send(KillMarker)
                                        closeScope = true
                                    }
                                }
                            }
                        }
                        if (receiveResult === KillMarker) {
                            lastWasKill = true
                        }
                        scopeChannel.send(receiveResult)
                    }
                    scopeChannel.close()
                    channel.close()
                }
            }
        }
    }

    suspend fun close() {
        jobManager.cancelAndJoinAll()
        scopes.clear()
    }

    fun isThreadBoundTo(thread: Thread, channel: Channel<SynchronizedExecutable<*>>): Boolean {
        return synchronized.synchronized {
            scopes.filterValues {
                it === channel
            }.run {
                if (entries.size == 1) {
                    thread.name.contains(entries.first().key.scopeIdentifier)
                } else if (entries.size == 0) {
                    false
                } else throw IllegalStateException("should never happen")
            }
        }
    }
}

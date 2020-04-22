package amber.sync.safeexec

import amber.sync.exec.SynchronizedExecutable
import amber.sync.exec.SynchronizedExecutorInterface
import kotlinx.coroutines.channels.Channel

class SafeSynchronizedExecutor(
        private val channel: Channel<SynchronizedExecutable<*>>, private val manager: SafeSynchronizedExecutorManger
) : SynchronizedExecutorInterface {

    override fun contains(thread: Thread) = manager.isThreadBoundTo(thread, channel)

    override suspend fun stop() {
        channel.close()
    }

    override suspend fun execute(synchronizedExecutable: SynchronizedExecutable<*>) {
        channel.send(synchronizedExecutable)
    }
}

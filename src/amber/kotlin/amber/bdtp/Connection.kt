package amber.bdtp

import amber.collections.copyAllValues
import amber.collections.sync
import amber.event.Event
import amber.sync.Synchronized
import amber.text.randomAlphanumericString
import amber.trial.trialAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class Connection(val input: Input, val output: Output) : Synchronized by Synchronized() {

    val onMessage = Event<MessageEvent>()
    val onClose = Event<Connection>()

    private val sessions = mutableListOf<Session>().sync(this)

    private val joinChannel = Channel<Unit>(1)

    lateinit var coroutineScope: CoroutineScope
    fun init(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    fun createSession(sessionIdLength: Int = 16): Session {
        var session = createSession(randomAlphanumericString(sessionIdLength))
        while (session == null) {
            session = createSession(randomAlphanumericString(sessionIdLength))
        }
        return session
    }

    fun createSession(sessionId: String): Session? = synchronized {
        if (containsInternal(sessionId)) return@synchronized null

        createAndGetSession(sessionId)
    }

    fun contains(sessionId: String) = synchronized {
        containsInternal(sessionId)
    }

    suspend fun send(frame: Frame) {
        try {

            if (frame.sessionId.isBlank()) frame.modify { sessionId = "" }
            else get(frame.sessionId)

            output.send(frame.toString())

        } catch (t: Throwable) {
            close()
        }
    }

    fun sendSync(frame: Frame) = runBlocking {
        send(frame)
    }

    internal fun close() {
        runBlocking {
            onClose.fire(this@Connection)
            val sessions = sessions.asyncSynchronized {
                mutableListOf<Session>().apply { copyAllValues(sessions) }
            }
            sessions.forEach {
                close(it)
            }
            input.cancel()
            output.cancel()
            trialAsync {
                joinChannel.send(Unit)
            }
            onMessage.clear()
            onClose.clear()
        }
    }

    operator fun get(sessionId: String) = synchronized {
        createAndGetSession(sessionId)
    }

    suspend fun close(sessionId: String) = asyncSynchronized {
        if (!containsInternal(sessionId)) return@asyncSynchronized

        val session = get(sessionId)
        session.close()
        sessions.remove(session)
    }

    fun close(session: Session) = synchronized {
        sessions.remove(session)
    }

    fun closeInput() {
        input.cancel()
    }

    suspend fun join() {
        joinChannel.receive()
        joinChannel.close()
    }

    private fun containsInternal(sessionId: String): Boolean {
        return !sessions.none { it.id == sessionId }
    }

    private fun createAndGetSession(sessionId: String): Session {
        if (sessionId.isBlank()) return Session("", this)

        createSessionInternal(sessionId)
        return getSessionInternal(sessionId)
    }

    private fun createSessionInternal(sessionId: String) {
        if (!sessions.none { it.id == sessionId }) return

        val session = Session(sessionId, this)
        sessions.add(session)
        sendSyncInternal(frame {
            session(session)
            method(Method.Success.SessionCreationSuccessfully())
        })
    }

    private fun getSessionInternal(sessionId: String): Session = sessions.find { it.id == sessionId }!!

    private suspend fun sendInternal(frame: Frame) {
        try {
            if (frame.sessionId.isBlank()) frame.modify { sessionId = "" }

            output.send(frame.toString())
        } catch (t: Throwable) {
            close()
        }
    }

    private fun sendSyncInternal(frame: Frame) = runBlocking {
        sendInternal(frame)
    }
}

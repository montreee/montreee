package amber.bdtp

import amber.collections.sync
import amber.event.Event
import amber.sync.Synchronized

class Session(val id: String, connection: Connection) : Synchronized by Synchronized() {

    private var connection: Connection? = connection

    val onMessage = Event<MessageEvent>()

    private val messages = mutableListOf<Message>().sync(this)

    internal suspend fun receive(input: Frame): Message = asyncSynchronized {
        if (input.packageNumber.isNotBlank() && input.totalPackages.isNotBlank()) {
            val message: Message = resolveMessage(input)
            message.receive(Package(input.packageNumber.toLong(), input.content), input.totalPackages.toLong())
            message
        } else if (resolveOpenMessage(input) == null) {
            val message: Message = resolveMessage(input)
            message.receive(Package(1, input.content), 1)
            message
        } else resolveMessage(input)
    }


    internal fun complete(message: Message) {
        messages.remove(message)
    }

    private fun resolveMessage(input: Frame): Message = synchronized {
        if (!messages.none { it.id == input.messageId }) return@synchronized messages.find { it.id == input.messageId }!!

        val message = Message(this, input.messageId, input.contentType, input.path)
        messages.add(message)
        return@synchronized message
    }

    fun resolveOpenMessage(input: Frame): Message? {
        var result: Message? = null
        synchronized {
            result = messages.find { it.id == input.messageId }
        }
        return result
    }

    fun closeOpenMessage(message: Message) = synchronized {
        messages.remove(message)
    }

    fun close() = synchronized {
        sendSync(frame { method(Method.Close.Session()) })
        closeInternal()
    }

    internal fun closeInternal() = synchronized {
        messages.clear()
        connection?.close(this)
        connection = null
    }

    suspend fun send(frame: Frame) {
        connection?.send(frame.modify { session(this@Session) })
    }

    fun sendSync(frame: Frame) = synchronized {
        connection?.sendSync(frame.modify {
            session(this@Session)
        })
    }
}

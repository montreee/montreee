package amber.bdtp

import amber.collections.copyAllValues
import amber.collections.syncMutableListOf
import amber.coroutines.JobManger
import amber.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Engine(private val coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    private val _onConnect = Event<Connection>()
    val onConnect = _onConnect.toSaveEvent()

    private val _onMessage = Event<MessageEvent>()
    val onMessage = _onMessage.toSaveEvent()

    private val _onMethod = Event<MethodEvent>()
    val onMethod = _onMethod.toSaveEvent()

    init {
        onMessage {
            connection.onMessage.fire(this)
            session.onMessage.fire(this)
        }
        onMethod.add(EngineMethodListener(this))
    }

    private val connections = syncMutableListOf<Connection>()

    private val connectionJobs = JobManger()

    fun connect(connection: Connection) {
        connection.init(coroutineScope)
        connections.add(connection)
        with(connectionJobs) {
            launch {
                connection.send(frame {
                    method(Method.Success.Connected())
                })
                for (it in connection.input) {
                    receive(connection, it)
                }
            }
        }
        connection.onClose {
            connections.remove(this)
        }
        _onConnect.fire(connection)
    }

    fun close(connection: Connection) {
        runBlocking {
            connection.close()
        }
    }

    fun close() {
        mutableListOf<Connection>().apply { copyAllValues(connections) }.forEach {
            close(it)
        }
    }

    fun send(connection: Connection, frame: Frame) {
        launch {
            connection.send(frame)
        }
    }

    private suspend fun receive(connection: Connection, value: String) {
        val input = frame(value)
        with(input) {
            try {
                if (method.isBlank()) {
                    var session: Session? = null
                    connection.asyncSynchronized {
                        session = connection[sessionId]
                    }
                    val message = session!!.receive(input)

                    if (!message.isComplete()) return

                    session?.complete(message)
                    _onMessage.fire(MessageEvent(input, connection, session!!, message))

                    return
                }

                val method = Method.parse(method)
                var session: Session? = null
                var message: Message? = null
                connection.asyncSynchronized {
                    if (!connection.contains(sessionId)) return@asyncSynchronized

                    session = connection[sessionId]
                    message = session!!.resolveOpenMessage(input)
                }
                _onMethod.fire(MethodEvent(input, connection, session, message, method))

            } catch (e: BdtpError) {
                connection.send(frame {
                    method(Method.Error(e.code))

                    if (e.message.isNullOrBlank()) return@frame

                    content(e.message)
                })
            } catch (e: Exception) {
                connection.send(frame {
                    method(Method.Error.InternalError())

                    if (e.message.isNullOrBlank()) return@frame

                    content(e.message ?: "")
                })
            }
        }
    }
}

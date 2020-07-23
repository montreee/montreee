package amber.bdtp

import amber.event.Listener
import kotlinx.coroutines.launch

internal class EngineMethodListener(private val engine: Engine) : Listener<MethodEvent> {

    override fun MethodEvent.on() {
        engine.launch {
            try {
                handleMethod()
            } catch (e: BdtpError) {
                handleBdtpError(e)
            }
        }
    }

    private suspend fun MethodEvent.handleMethod() {
        when (method) {
            is Method.Ping    -> handlePingMethod()
            is Method.Close   -> handleCloseMethod()
            is Method.Error   -> handleErrorMethod()
            is Method.Success -> handleSuccessMethod()
        }
    }

    private suspend fun MethodEvent.handlePingMethod() {
        when (method.code) {
            Method.Ping.Code.PING -> {
                connection?.send(frame {
                    method(Method.Ping.Pong())
                })
            }
            Method.Ping.Code.PONG -> {
            }
        }
    }

    private suspend fun MethodEvent.handleCloseMethod() {
        when (method.code) {
            Method.Close.Code.CLOSE_CONNECTION -> {
                if (connection == null) return

                connection.closeInput()
                connection.send(frame {
                    method(Method.Close.Connection())
                })
                connection.close()
            }
            Method.Close.Code.CLOSE_SESSION    -> {
                if (connection == null || session == null) return

                session.closeInternal()
            }
            Method.Close.Code.CLOSE_MESSAGE    -> {
                if (connection == null || session == null || message == null) return

                session.closeOpenMessage(message)
            }
            else                               -> throw BdtpError(0)
        }
    }

    private suspend fun MethodEvent.handleErrorMethod() {
        when (method.code) {
            Method.Error.Code.SYNTAX_ERROR            -> {
            }
            Method.Error.Code.CONNECTION_FAILED       -> {
            }
            Method.Error.Code.SESSION_CREATION_FAILED -> {
            }
            Method.Error.Code.MESSAGE_FAILED          -> {
            }
            Method.Error.Code.INTERNAL_ERROR          -> {
            }
            else                                      -> throw BdtpError(0)
        }
    }

    private suspend fun MethodEvent.handleSuccessMethod() {
        when (method.code) {
            Method.Success.Code.CONNECTED        -> {
            }
            Method.Success.Code.SESSION_CREATED  -> {
            }
            Method.Success.Code.MESSAGE_RECEIVED -> {
            }
            else                                 -> throw BdtpError(0)
        }
    }

    private suspend fun MethodEvent.handleBdtpError(error: BdtpError) {
        connection?.send(frame {
            method(Method.Error(error.code))
        })
    }
}
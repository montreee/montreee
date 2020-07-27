package amber.bdtp.impl

import amber.bdtp.Connection
import amber.bdtp.Engine
import amber.bdtp.Input
import amber.bdtp.Output
import amber.coroutines.scope
import amber.server.Server
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.launch

class LocalImpl(val server: Engine, val client: Engine) : Server {
    override fun start() {
        with(Dispatchers.Default.scope()) {
            val serverInput = Input()
            val serverOut = Output()
            val serverConnection = Connection(serverInput, serverOut)
            val clientInput = Input()
            val clientOut = Output()
            val clientConnection = Connection(clientInput, clientOut)
            fun closeServer() {
                serverInput.close()
                serverOut.close()
            }

            fun closeClient() {
                clientInput.close()
                clientOut.close()
            }
            serverConnection.onClose {
                closeServer()
            }
            clientConnection.onClose {
                closeClient()
            }
            launch {
                try {
                    for (frame in clientOut) {
                        serverInput.send(frame)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    closeClient()
                } catch (e: ClosedSendChannelException) {
                    closeClient()
                } catch (e: Throwable) {
                    closeClient()
                }
            }
            launch {
                try {
                    for (frame in serverOut) {
                        clientInput.send(frame)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    closeServer()
                } catch (e: ClosedSendChannelException) {
                    closeServer()
                } catch (e: Throwable) {
                    closeServer()
                }
            }
            server.connect(serverConnection)
            client.connect(clientConnection)
        }
    }

    override fun stop() {

    }

}

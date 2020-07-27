package amber.bdtp.impl

import amber.bdtp.Connection
import amber.bdtp.Engine
import amber.bdtp.Input
import amber.bdtp.Output
import amber.coroutines.joinAllJobs
import amber.crypt.KeyPair
import amber.crypt.aes.AESKeyPairGenerator
import amber.crypt.aes.AESPrivateKey
import amber.crypt.aes.AESPublicKey
import amber.crypt.rsa.RSAKeyPairGenerator
import amber.crypt.rsa.RSAPublicKey
import amber.ktor.embeddedServer
import amber.logging.Logger
import amber.logging.adapter.Log4JAdapter
import amber.logging.error
import amber.logging.trace
import amber.server.Server
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import io.ktor.websocket.WebSockets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.util.concurrent.TimeUnit

class KtorWSBdtpServer(
        private val engine: Engine,
        private val port: Int,
        private val encryption: Boolean = false,
        private val logger: Logger? = null,
        private val coroutineScope: CoroutineScope
) : Server {

    val ktor = coroutineScope.embeddedServer(
            Netty, port, logger = Log4JAdapter(
            if (logger != null) Logger(logger.name + " log4j adapter", logger) else Logger(
                    "KtorWSBdtpServer Logger log4j adapter"
            )
    )
    ) {
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
            pingPeriod = Duration.ofSeconds(60)
            timeout = Duration.ofSeconds(60)
        }
        routing {
            webSocket("/") {
                createConnection(engine, logger, coroutineScope, encryption = encryption)
            }
        }
    }

    override fun start() {
        ktor.start()
    }

    override fun stop() {
        ktor.stop(1, 1, TimeUnit.NANOSECONDS)
    }
}

class KtorWSBdtpClinet(
        val engine: Engine,
        private val logger: Logger? = null,
        val coroutineScope: CoroutineScope
) : Server {

    override fun start() {}

    fun connect(host: String, port: Int, encryption: Boolean = false): Connection {
        var connection: Connection? = null
        val errorChannel = Channel<Exception?>(3)
        runBlocking {
            val createdConnectionChannel = Channel<Connection>(1)
            coroutineScope.launch {
                val client = HttpClient(CIO) {
                    install(io.ktor.client.features.websocket.WebSockets)
                    engine {
                        endpoint.connectTimeout = 100
                        endpoint.keepAliveTime = 1000
                    }
                }
                try {
                    client.ws(
                            method = HttpMethod.Get, host = host, port = port, path = "/"
                    ) {
                        errorChannel.send(null)
                        createConnection(engine, logger, coroutineScope, createdConnectionChannel, encryption)
                    }
                } catch (e: Exception) {
                    errorChannel.send(e)
                }
                client.close()
            }
            val error = errorChannel.receive()
            errorChannel.close()
            if (error != null) {
                createdConnectionChannel.close()
                throw error
            }
            connection = createdConnectionChannel.receive()
            createdConnectionChannel.close()
        }
        return connection!!
    }

    override fun stop() {}
}

private suspend fun DefaultWebSocketSession.createConnection(
        engine: Engine,
        logger: Logger? = null,
        coroutineScope: CoroutineScope,
        createdConnectionChannel: Channel<Connection>? = null,
        encryption: Boolean = false
) {
    val trafficLogger = if (logger != null) Logger("KtorWSBdtp Traffic Logger", logger) else null

    if (encryption) handleEncryptedConnection(engine, coroutineScope, trafficLogger, createdConnectionChannel)
    else handleNotEncryptedConnection(engine, coroutineScope, trafficLogger, createdConnectionChannel)
}

class ConnectionEncryptionEstablishingException(cause: Throwable? = null) : Exception(
        "Failed to establish encrypted connection.", cause
)

private suspend fun DefaultWebSocketSession.handleEncryptedConnection(
        engine: Engine,
        coroutineScope: CoroutineScope,
        trafficLogger: Logger?,
        createdConnectionChannel: Channel<Connection>?
) {
    val myRSAKeyPairGenerator = RSAKeyPairGenerator(64)
    val myRSAKeyPair = myRSAKeyPairGenerator()
    val myAESKeyPairGenerator = AESKeyPairGenerator(128)
    val myAESKeyPair = myAESKeyPairGenerator()
    var thereRSAPublicKey: RSAPublicKey? = null
    var thereAESSecretKey: KeyPair<AESPrivateKey, AESPublicKey>? = null

    val RSAKeyReceivedChannel = Channel<Unit>(1)
    joinAllJobs {
        launch {
            try {

                val frameRSAKey = (incoming.receive() as Frame.Binary)

                val byteArrayRSAKey = frameRSAKey.data

                thereRSAPublicKey = myRSAKeyPairGenerator.importPublic(byteArrayRSAKey)
                trafficLogger?.trace {
                    +"public rsa key received: ${String(byteArrayRSAKey)}"
                }
                RSAKeyReceivedChannel.send(Unit)

                val frameAESKey = (incoming.receive() as Frame.Binary)

                val byteArrayAESKey = myRSAKeyPair.decrypt(frameAESKey.data)

                val private = myAESKeyPairGenerator.importPrivate(byteArrayAESKey)
                val public = myAESKeyPairGenerator.importPublic(byteArrayAESKey)
                thereAESSecretKey = KeyPair(private, public)
                trafficLogger?.trace {
                    +"aes secret key received: ${String(byteArrayAESKey)}"
                }

            } catch (e: Throwable) {
                throw ConnectionEncryptionEstablishingException(cause = e)
            }
        }
        launch {
            outgoing.send(Frame.Binary(true, myRSAKeyPairGenerator.exportPublic(myRSAKeyPair.public)))

            RSAKeyReceivedChannel.receive()

            val encryptedKey = thereRSAPublicKey?.encrypt(myAESKeyPairGenerator.exportPrivate(myAESKeyPair.private))
            outgoing.send(Frame.Binary(true, encryptedKey!!))
        }
    }

    val input = Input()
    val out = Output()
    val connection = Connection(input, out)
    fun close() {
        input.close()
        out.close()
        incoming.cancel()
        outgoing.close()
        engine.close(connection)
    }


    engine.connect(connection)
    createdConnectionChannel?.send(connection)
    coroutineScope.launch {
        try {
            for (frame in incoming) {
                val binaryFrame = (frame as Frame.Binary)
                trafficLogger?.trace {
                    +"receive(encrypted): ${String(binaryFrame.data)}"
                }

                val decrypted = thereAESSecretKey?.decrypt(binaryFrame.data)
                val text = String(decrypted ?: ByteArray(0))
                input.send(text)
                trafficLogger?.trace {
                    +"receive(decrypted): $text"
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            close()
        } catch (e: ClosedSendChannelException) {
            close()
        } catch (e: Throwable) {
            trafficLogger?.error(e)
            close()
        } finally {
            close()
        }
    }
    coroutineScope.launch {
        try {
            for (frame in out) {
                trafficLogger?.trace(
                        "send(decrypted): $frame"
                )

                val encrypted = myAESKeyPair.encrypt(frame.toByteArray())
                outgoing.send(Frame.Binary(true, encrypted))
                trafficLogger?.trace(
                        "send(encrypted): ${String(encrypted)}"
                )
            }
        } catch (e: ClosedReceiveChannelException) {
            close()
        } catch (e: ClosedSendChannelException) {
            close()
        } catch (e: Throwable) {
            trafficLogger?.error(e)
            close()
        } finally {
            close()
        }
    }
    connection.join()
}

private suspend fun DefaultWebSocketSession.handleNotEncryptedConnection(
        engine: Engine,
        coroutineScope: CoroutineScope,
        trafficLogger: Logger?,
        createdConnectionChannel: Channel<Connection>?
) {
    val input = Input()
    val out = Output()
    val connection = Connection(input, out)
    fun close() {
        input.close()
        out.close()
        incoming.cancel()
        outgoing.close()
        engine.close(connection)
    }

    coroutineScope.launch {
        try {
            for (frame in incoming) {
                val text = (frame as Frame.Text).readText()
                input.send(text)
                trafficLogger?.trace { +"receive: ${String(frame.data)}" }
            }
        } catch (e: ClosedReceiveChannelException) {
            close()
        } catch (e: ClosedSendChannelException) {
            close()
        } catch (e: Throwable) {
            trafficLogger?.error(e)
            close()
        } finally {
            close()
        }
    }
    coroutineScope.launch {
        try {
            for (frame in out) {
                outgoing.send(Frame.Text(frame))
                trafficLogger?.trace { +"send: ${frame}" }
            }
        } catch (e: ClosedReceiveChannelException) {
            close()
        } catch (e: ClosedSendChannelException) {
            close()
        } catch (e: Throwable) {
            trafficLogger?.error(e)
            close()
        } finally {
            close()
        }
    }
    engine.connect(connection)
    createdConnectionChannel?.send(connection)
    connection.join()
}

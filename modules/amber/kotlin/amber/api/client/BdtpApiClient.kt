package amber.api.client

import amber.api.MethodPath
import amber.api.ParameterList
import amber.api.response.Response
import amber.api.response.TextResponse
import amber.bdtp.Connection
import amber.bdtp.frame
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

class BdtpApiClient(private val connection: Connection, private var requestTimeout: Long = -1) : ApiClient {

    override fun connect() {
    }

    suspend fun call(methodPath: MethodPath, parameter: ParameterList, requestTimeout: Long): Response {
        val session = connection.createSession()
        val responseChannel = Channel<Response>(1)
        session.onMessage {
            runBlocking {
                responseChannel.send(TextResponse(message.read()))
            }
        }

        session.send(frame {
            path = "$methodPath${if (parameter.isEmpty()) "" else "?"}${parameter.entries.joinToString(
                    separator = "&"
            ) { "${it.key}=${it.value}" }}"
        })
        return (if (requestTimeout < 0) return responseChannel.receive()
        else withTimeout(requestTimeout) {
            responseChannel.receive()
        }).apply {
            session.close()
        }
    }

    override suspend fun call(methodPath: MethodPath, parameter: ParameterList): Response =
            call(methodPath, parameter, requestTimeout)

    override fun disconnect() {
        runBlocking { connection.close() }
    }
}
package amber.api.client

import amber.api.MethodPath
import amber.api.ParameterList
import amber.api.response.Response
import amber.bdtp.Engine
import amber.bdtp.impl.KtorWSBdtpClinet
import amber.coroutines.OptimalCoroutineScope
import amber.logging.Logger
import kotlinx.coroutines.CoroutineScope

class KtorWSBdtpApiClient(
        private val host: String,
        private val port: Int,
        val encryption: Boolean = false,
        val logger: Logger = Logger("KtorWSBdtpApiClient Logger"),
        val requestTimeout: Long = -1,
        val coroutineScope: CoroutineScope = OptimalCoroutineScope("KtorWSBdtpApiClient Worker")
) : ApiClient {

    val engine = Engine(coroutineScope)

    private val clientBdtpImpl = KtorWSBdtpClinet(engine, logger, coroutineScope)

    private lateinit var client: BdtpApiClient

    override fun connect() {
        clientBdtpImpl.start()
        client = BdtpApiClient(
                clientBdtpImpl.connect(host, port, encryption),
                requestTimeout = requestTimeout
        )
        client.connect()
    }

    override suspend fun call(methodPath: MethodPath, parameter: ParameterList): Response =
            client.call(methodPath, parameter)

    override fun disconnect() {
        engine.close()
        client.disconnect()
        clientBdtpImpl.stop()
    }
}
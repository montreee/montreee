package amber.api.server.impl.bdtp

import amber.api.ParameterList
import amber.api.server.Api
import amber.api.server.ApiImpl
import amber.api.server.constructCall
import amber.bdtp.Engine
import amber.bdtp.MessageEvent
import amber.event.Listener
import amber.server.Server
import kotlinx.coroutines.runBlocking

class BdtpApiImpl(
        override val api: Api, private val engine: Engine
) : ApiImpl, Server {

    private var onMessageListener: Listener<MessageEvent>? = null

    override fun start() {
        onMessageListener = engine.onMessage {
            if (raw.path.isBlank()) return@onMessage

            val destination = raw.path.substringBefore("?")
            val parameter = mutableMapOf<String, String>().apply {
                val rawParameter = raw.path.substringAfter("?")
                rawParameter.split("&").forEach {
                    put(it.substringBefore("="), it.substringAfter("="))
                }
            }
            runBlocking(context = engine.coroutineContext) {
                api.call(destination, constructCall(BdtpApiImplSession(this@onMessage), ParameterList().apply {
                    parameter.forEach {
                        it.key to it.value
                    }
                }))
            }
        }
    }

    override fun stop() {
        if (onMessageListener == null) return
        engine.onMessage.remove(onMessageListener!!)
    }
}

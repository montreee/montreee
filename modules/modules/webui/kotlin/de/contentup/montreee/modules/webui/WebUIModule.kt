package de.contentup.montreee.modules.webui

import amber.coroutines.scope
import amber.ktor.embeddedServer
import amber.logging.Logger
import amber.logging.adapter.Log4JAdapter
import amber.logging.adapter.Slf4JLogBinder
import de.contentup.montreee.Version
import de.contentup.montreee.module.MontreeeModule
import de.contentup.montreee.modules.webui.app.application
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit

class WebUIModule(version: Version) : MontreeeModule("Montreee WebUI Module", version) {

    private val serverLogger by lazy { Log4JAdapter(Logger("WebUi Server Logger", logger)) }
    private val server by lazy {
        context.network.scope().embeddedServer(
                Netty, logger = serverLogger, port = config.webui.port.toInt()
        ) {
            application()
        }
    }

    override fun launch() {
        Slf4JLogBinder.logger = Logger("slf4j", logger)

        server.start()
    }

    override fun exit() {
        server.stop(0, 0, TimeUnit.MILLISECONDS)
    }
}

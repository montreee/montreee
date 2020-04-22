package de.contentup.montreee.modules.standalone

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

class StandaloneModule(version: Version) : MontreeeModule("Montreee Standalone Module", version) {

    private val webuiServerLogger by lazy { Log4JAdapter(Logger("Standalone WebUi Server Logger", logger)) }
    private val webuiServer by lazy {
        context.network.scope().embeddedServer(
                Netty, logger = webuiServerLogger, port = config.webui.port.toInt()
        ) {
            application()
        }
    }

    override fun launch() {
        Slf4JLogBinder.logger = Logger("slf4j", logger)

        webuiServer.start()
    }

    override fun exit() {
        webuiServer.stop(0, 0, TimeUnit.MILLISECONDS)
    }
}

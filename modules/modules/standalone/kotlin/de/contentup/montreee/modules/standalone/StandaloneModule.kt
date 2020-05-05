package de.contentup.montreee.modules.standalone

import amber.coroutines.scope
import amber.ktor.embeddedServer
import amber.logging.Logger
import amber.logging.adapter.Log4JAdapter
import amber.logging.adapter.Slf4JLogBinder
import de.contentup.montreee.Version
import de.contentup.montreee.module.MontreeeModule
import de.contentup.montreee.modules.webui.TestData
import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.application
import de.contentup.montreee.modules.webui.repository.impl.InMemoryListRepository
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit

class StandaloneModule(version: Version) : MontreeeModule("Montreee Standalone Module", version) {

    private val applicationContext = ApplicationContext(
            InMemoryListRepository(TestData.list)
    )

    private val webuiServerLogger by lazy { Log4JAdapter(Logger("Standalone WebUi Server Logger", logger)) }
    private val webuiServer by lazy {
        context.network.scope().embeddedServer(
                Netty, logger = webuiServerLogger, port = config.webui.port.toInt()
        ) {
            application(applicationContext)
        }
    }

    override fun launch() {
        Slf4JLogBinder.register(logger)

        webuiServer.start()
    }

    override fun exit() {
        webuiServer.stop(0, 0, TimeUnit.MILLISECONDS)
    }
}

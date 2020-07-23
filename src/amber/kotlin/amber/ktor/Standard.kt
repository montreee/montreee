package amber.ktor

import io.ktor.application.*
import io.ktor.server.engine.*
import kotlinx.coroutines.CoroutineScope
import org.slf4j.Logger

fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration> CoroutineScope.embeddedServer(
        factory: ApplicationEngineFactory<TEngine, TConfiguration>,
        port: Int = 80,
        host: String = "0.0.0.0",
        watchPaths: List<String> = emptyList(),
        configure: TConfiguration.() -> Unit = {},
        logger: Logger,
        module: Application.() -> Unit
): TEngine {
    val environment = applicationEngineEnvironment {
        this.parentCoroutineContext = coroutineContext
        this.log = logger
        this.watchPaths = watchPaths
        this.module(module)

        connector {
            this.port = port
            this.host = host
        }
    }

    return io.ktor.server.engine.embeddedServer(factory, environment, configure)
}

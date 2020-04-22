package amber.logging.adapter

import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import org.slf4j.spi.LoggerFactoryBinder

object Slf4JLogBinder : LoggerFactoryBinder {

    var logger: amber.logging.Logger? = null

    private object LoggerFactory : ILoggerFactory {
        override fun getLogger(name: String?): Logger {
            return Log4JAdapter(
                    amber.logging.Logger(
                            name ?: "null", logger
                    )
            )
        }
    }

    override fun getLoggerFactory(): ILoggerFactory = LoggerFactory

    override fun getLoggerFactoryClassStr() = ""
}

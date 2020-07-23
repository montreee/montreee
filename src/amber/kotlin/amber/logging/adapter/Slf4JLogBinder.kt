package amber.logging.adapter

import amber.logging.Logger
import org.slf4j.ILoggerFactory
import org.slf4j.spi.LoggerFactoryBinder
import org.slf4j.Logger as Slf4jLogger

object Slf4JLogBinder : LoggerFactoryBinder {

    private var logger: Logger? = null

    fun register(parent: Logger) {
        logger = Logger("slf4j", parent)
    }

    private object LoggerFactory : ILoggerFactory {
        override fun getLogger(name: String?): Slf4jLogger = Log4JAdapter(Logger(name.toString(), logger))
    }

    override fun getLoggerFactory(): ILoggerFactory = LoggerFactory

    override fun getLoggerFactoryClassStr() = ""
}

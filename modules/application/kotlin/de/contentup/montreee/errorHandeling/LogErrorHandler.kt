package de.contentup.montreee.errorHandeling

import amber.logging.Logger
import amber.logging.error
import amber.trial.trial

class LogErrorHandler(private val logger: Logger) : Thread.UncaughtExceptionHandler {

    class NotAbleToLogUncaughtExceptionException(throwable: Throwable) : Exception(
            "LogErrorHandler wasn't able to Log UncaughtException properly.", throwable
    )

    override fun uncaughtException(t: Thread?, e: Throwable) {
        trial {
            logger.error(e)
        } alternate { throw NotAbleToLogUncaughtExceptionException(e) }
    }
}

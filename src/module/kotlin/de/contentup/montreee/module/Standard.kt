package de.contentup.montreee.module

import amber.logging.exception
import amber.logging.info
import de.contentup.montreee.Application
import kotlinx.coroutines.delay

suspend fun MontreeeModule.asyncAutoRetry(block: suspend () -> Unit) {
    var isRetry = false
    val retryDelay = config.autoRetryDelay
    while (config.autoRetry || !isRetry) {
        try {
            block()
            break
        } catch (e: Throwable) {
            if (!config.autoRetry) {
                logger.exception(e)
                isRetry = true
            } else {
                logger.info {
                    +"${e.message} retrying in $retryDelay ms"
                }
            }
            delay(retryDelay.toLong())
        }
    }
}

inline fun <T> T.runIfDevelopmentIsEnabled(block: T.() -> T): T {
    return if (Application.module.config.development) block() else this
}

inline fun <T> T.runIfDevelopmentIsDisabled(block: T.() -> T): T {
    return if (!Application.module.config.development) block() else this
}

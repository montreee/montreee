@file:JvmName("StaticLoggerBinder")

package org.slf4j.impl

import amber.logging.adapter.Slf4JLogBinder
import org.slf4j.spi.LoggerFactoryBinder

object StaticLoggerBinder : LoggerFactoryBinder by Slf4JLogBinder {
    @JvmStatic
    fun getSingleton() = this
}

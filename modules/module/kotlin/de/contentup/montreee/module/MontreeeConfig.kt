package de.contentup.montreee.module

import amber.config.*

class MontreeeConfig(vararg impl: Config) : MontreeeConfigBase(*impl) {

    val development by boolean()

    val dir = DirsConfig(this)

    class DirsConfig(config: Config) : Config by config.sub("dir") {
        val work by string()
        val plugins by string()
        val data by string()
        val webui by string()
    }

    val port by number()
    val host by string()

    val encryption by boolean()

    val autoRetry by boolean("auto_retry")
    val autoRetryDelay by number("auto_retry_delay")

    val core = CoreConfig(this)

    class CoreConfig(config: Config) : Config by config.sub("core") {
        val port by fallback(number(), config.number())
        val host by fallback(string(), config.string())
    }

    val render = RenderConfig(this)

    class RenderConfig(config: Config) : Config by config.sub("render") {
        val port by fallback(number(), config.number())
        val host by fallback(string(), config.string())
    }

    val webui = WebuiConfig(this)

    class WebuiConfig(config: Config) : Config by config.sub("webui") {
        val port by fallback(number(), config.number())
        val host by fallback(string(), config.string())
    }
}
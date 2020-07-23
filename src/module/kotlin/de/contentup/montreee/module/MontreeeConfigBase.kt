package de.contentup.montreee.module

import amber.config.Config
import amber.config.impl.ReplacePropertiesConfig
import amber.config.impl.StackConfig

open class MontreeeConfigBase(vararg impl: Config) : Config {
    private val stackConfig = StackConfig(*impl)
    private val backend = ReplacePropertiesConfig(stackConfig)
    override fun find(path: String) = backend.find(path)
    fun load(config: Config) = stackConfig.load(config)
}

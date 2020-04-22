package amber.config.impl

import amber.config.Config

class LazyConfig(block: () -> Config) : Config {
    private val impl: Config by lazy(block)
    override fun find(path: String) = impl.find(path)
}
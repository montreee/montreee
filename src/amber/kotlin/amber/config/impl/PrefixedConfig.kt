package amber.config.impl

import amber.config.Config

class PrefixedConfig(private var prefix: String, private val impl: Config) : Config {
    init {
        if (!prefix.endsWith(".")) prefix += "."
    }

    override fun find(path: String) = impl["$prefix$path"]
}

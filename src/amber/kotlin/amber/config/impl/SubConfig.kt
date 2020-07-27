package amber.config.impl

import amber.config.Config

class SubConfig(private val prefix: String, private val impl: Config) : Config by PrefixedConfig(prefix, impl)

package amber.config.impl

import amber.config.Config

open class MapConfig(private val map: MutableMap<String, String> = mutableMapOf()) : Config {
    override fun find(path: String) = map[path]
}

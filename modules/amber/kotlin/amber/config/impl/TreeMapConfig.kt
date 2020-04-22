package amber.config.impl

import amber.config.Config
import amber.trial.trial

open class TreeMapConfig(protected var map: Map<String, Any?> = emptyMap()) : Config {
    override fun find(path: String): String? {
        var map: Map<String, Any?>? = map
        var ret: String? = null
        path.split(".").forEach {
            val localCurrentMap = map ?: return@find null
            if (!localCurrentMap.contains(it)) return null
            val item = localCurrentMap[it]
            if (item is Map<*, *>) {
                map = trial<Map<String, Any?>?> { (item as Map<String, Any?>) } alternate { null }
            } else {
                ret = when (item) {
                    is String  -> item
                    is Boolean -> item.toString()
                    is Number  -> item.toString()
                    else       -> null
                }
            }
        }
        return ret
    }
}
package amber.config.delegate

import amber.config.Config
import amber.config.Delegate
import kotlin.reflect.KProperty

open class BooleanDelegate(config: Config, path: String = "") : Delegate<Boolean> {
    private val backend = StringDelegate(config, path)
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) =
            backend.getValue(thisRef, property).toBoolean()
}
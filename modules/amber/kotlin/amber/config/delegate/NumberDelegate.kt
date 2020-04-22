package amber.config.delegate

import amber.config.Config
import amber.config.Delegate
import kotlin.reflect.KProperty

open class NumberDelegate(config: Config, path: String = "") : Delegate<Number> {
    private val backend = StringDelegate(config, path)
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Number =
            backend.getValue(thisRef, property).toDouble()
}
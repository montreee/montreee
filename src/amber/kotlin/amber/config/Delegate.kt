package amber.config

import kotlin.reflect.KProperty

interface Delegate<T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T
}

package amber.properties

import amber.properties.value.PropertyValue
import kotlin.reflect.KProperty

abstract class AccessiblePropertyBase<T>(initValue: PropertyValue<T>) : PropertyBase<T>(initValue) {

    var value by this

    protected var valueObject: PropertyValue<T>
        set(value) {
            setSave(value)
        }
        get() {
            return getSave()
        }

    open fun set(value: T) {
        setSave(PropertyValue(value))
    }

    open fun get(): T {
        return getSave().value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        set(value)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }
}

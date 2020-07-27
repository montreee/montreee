package amber.properties

import amber.properties.value.InternalValueAccess
import amber.properties.value.PropertyValue
import amber.sync.Synchronized

abstract class PropertyBase<T>(initValue: PropertyValue<T>) : Synchronized by Synchronized() {

    @Volatile
    private var _value: PropertyValue<T> = initValue

    //TODO REDO internal value access
    private val internalValueAccess by lazy { InternalValueAccess<T> { setInternalValue(it) } }

    private fun setInternalValue(value: PropertyValue<T>) = synchronized {
        _value = value
    }

    private fun getInternalValue(): PropertyValue<T> = synchronized {
        _value
    }


    protected fun setSave(value: PropertyValue<T>) = synchronized {
        checkSet(value)
        if (value.value !== getInternalValue().value) changeValue(value, internalValueAccess)
    }

    //todo add annotation don't call external
    protected open fun changeValue(value: PropertyValue<T>, internalValueAccess: InternalValueAccess<T>) =
            synchronized {
                internalValueAccess.setInternalValue(value)
            }

    protected open fun checkSet(newValue: PropertyValue<T>) {}

    protected open fun getSave(): PropertyValue<T> = synchronized {
        getInternalValue()
    }
}

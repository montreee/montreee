package amber.properties

import amber.properties.event.PropertyEventListener
import amber.properties.event.PropertyEventManager
import amber.properties.value.InternalValueAccess
import amber.properties.value.PropertyValue

abstract class ListenablePropertyBase<T>(initValue: PropertyValue<T>) : BufferedPropertyBase<T>(initValue) {

    private val eventManager by lazy { PropertyEventManager<T>(this) }

    override fun changeValue(value: PropertyValue<T>, internalValueAccess: InternalValueAccess<T>) {
        synchronized {
            val oldValue = valueObject
            eventManager.fireValueChangeEvent(oldValue, value)
            internalValueAccess.setInternalValue(value)
            val newValue = valueObject
            eventManager.fireValueChangedEvent(oldValue, newValue)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun addListener(listener: PropertyEventListener<T>) = synchronized {
        eventManager.add(listener)
    }

    fun removeListener(listener: PropertyEventListener<T>) = synchronized {
        eventManager.remove(listener)
    }
}

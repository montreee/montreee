package amber.properties.event

import amber.collections.forEachSave
import amber.properties.ListenablePropertyBase
import amber.properties.value.PropertyValue

class PropertyEventManager<T>(private val property: ListenablePropertyBase<T>) : ArrayList<PropertyEventListener<T>>() {

    override fun add(element: PropertyEventListener<T>): Boolean {
        element.onAddToProperty(property)
        return super.add(element)
    }

    fun fireValueChangeEvent(currentValue: PropertyValue<T>, newValue: PropertyValue<T>) {
        forEachSave {
            it.onValueChange(currentValue, newValue)
        }
    }

    fun fireValueChangedEvent(oldValue: PropertyValue<T>, newValue: PropertyValue<T>) {
        forEachSave {
            it.onValueChanged(oldValue, newValue)
        }
    }
}

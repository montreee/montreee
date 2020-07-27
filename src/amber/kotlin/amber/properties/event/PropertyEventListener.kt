package amber.properties.event

import amber.properties.ListenablePropertyBase
import amber.properties.exceptions.IllegalPropertyListenerAddPropertyException
import amber.properties.exceptions.NeverAddedPropertyException
import amber.properties.value.PropertyValue

abstract class PropertyEventListener<T> {

    private var _property: ListenablePropertyBase<T>? = null

    val property by lazy {
        _property ?: throw NeverAddedPropertyException()
    }

    internal fun onAddToProperty(property: ListenablePropertyBase<T>) {
        if (_property != null && property !== _property) throw IllegalPropertyListenerAddPropertyException()
        else _property = property
    }

    fun removeItself() {
        property.removeListener(this)
    }

    abstract fun onValueChange(currentValue: PropertyValue<T>, newValue: PropertyValue<T>)
    abstract fun onValueChanged(oldValue: PropertyValue<T>, newValue: PropertyValue<T>)
}

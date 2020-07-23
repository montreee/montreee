package amber.properties.groups

import amber.properties.BindablePropertyBase
import amber.properties.event.PropertyEventListener
import amber.properties.value.PropertyValue

class PropertyGroupElementListener<T>(val delegateTo: BindablePropertyBase<T>) : PropertyEventListener<T>() {
    override fun onValueChange(currentValue: PropertyValue<T>, newValue: PropertyValue<T>) {}

    override fun onValueChanged(oldValue: PropertyValue<T>, newValue: PropertyValue<T>) {
        delegateTo.value = newValue.value
    }
}

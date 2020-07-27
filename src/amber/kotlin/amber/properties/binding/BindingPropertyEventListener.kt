package amber.properties.binding

import amber.properties.BindablePropertyBase
import amber.properties.event.PropertyEventListener
import amber.properties.value.PropertyValue

class BindingPropertyEventListener<T>(
        val from: BindablePropertyBase<T>, val to: BindablePropertyBase<T>, val binding: Binding<T>
) : PropertyEventListener<T>() {

    override fun onValueChange(currentValue: PropertyValue<T>, newValue: PropertyValue<T>) {}

    override fun onValueChanged(oldValue: PropertyValue<T>, newValue: PropertyValue<T>) {
        binding.value = newValue.value
        from.value = newValue.value
    }
}

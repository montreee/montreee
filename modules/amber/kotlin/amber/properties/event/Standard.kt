package amber.properties.event

import amber.properties.ListenablePropertyBase
import amber.properties.value.PropertyValue

class PropertyLambdaListener<T>(
        val onValueChange: PropertyEventListener<T>.(T) -> Unit = {},
        val onValueChanged: PropertyEventListener<T>.(T) -> Unit = {}
) : PropertyEventListener<T>() {

    override fun onValueChange(currentValue: PropertyValue<T>, newValue: PropertyValue<T>) {
        onValueChange(newValue.value)
    }

    override fun onValueChanged(oldValue: PropertyValue<T>, newValue: PropertyValue<T>) {
        onValueChanged(newValue.value)
    }
}

fun <T> ListenablePropertyBase<T>.onChange(l: PropertyEventListener<T>.(T) -> Unit) {
    this.addListener(PropertyLambdaListener(onValueChange = l))
}

fun <T> ListenablePropertyBase<T>.onChanged(l: PropertyEventListener<T>.(T) -> Unit) {
    this.addListener(PropertyLambdaListener(onValueChanged = l))
}

package amber.properties

import amber.properties.value.PropertyValue

@Suppress("unused")
open class Property<T>(initValue: PropertyValue<T>) : BindablePropertyBase<T>(initValue) {

    constructor(value: T) : this(PropertyValue(value))
}

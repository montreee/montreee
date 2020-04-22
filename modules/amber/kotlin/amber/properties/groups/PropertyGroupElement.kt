package amber.properties.groups

import amber.properties.BindablePropertyBase

class PropertyGroupElement<T>(
        val property: BindablePropertyBase<T>,
        val internalProperty: BindablePropertyBase<T>,
        val propertyGroupsProperty: BindablePropertyBase<T>
) {

    val listener1 = PropertyGroupElementListener<T>(propertyGroupsProperty)
    val listener2 = PropertyGroupElementListener<T>(internalProperty)
    fun add() {
        internalProperty.addListener(listener1)
        propertyGroupsProperty.addListener(listener2)
    }

    fun remove() {
        listener1.removeItself()
        listener2.removeItself()
    }
}

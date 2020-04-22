package amber.properties.groups

import amber.collections.forEachSave
import amber.properties.BindablePropertyBase
import amber.properties.Property

class PropertyGroup<T>(initValue: T) {

    private val properties = ArrayList<PropertyGroupElement<T>>()

    private val property = Property<T>(initValue)

    constructor(initValue: T, vararg properties: BindablePropertyBase<T>) : this(initValue) {
        addAllValuesOf(properties)
    }

    constructor(vararg properties: BindablePropertyBase<T>) : this(properties[0].value) {
        addAllValuesOf(properties)
    }


    fun add(property: BindablePropertyBase<T>) {
        val internalProperty = Property<T>(this.property.value)
        property.binding = PropertyGroupBinding<T>(this, property, internalProperty)
        internalProperty.bind(property)
        val propertyGroupElement = PropertyGroupElement<T>(property, internalProperty, this.property)
        propertyGroupElement.add()
        properties.add(propertyGroupElement)
    }

    fun addAllValuesOf(properties: Array<out BindablePropertyBase<T>>) {
        properties.forEach {
            add(it)
        }
    }

    fun addAll(vararg properties: BindablePropertyBase<T>) = addAllValuesOf(properties)

    fun remove(property: BindablePropertyBase<T>) {
        if (property.isBound) property.unbind()
        properties.forEachSave {
            if (it.property === property) {
                it.remove()
                it.internalProperty.unbind()
                properties.remove(it)
            }
        }
    }

    fun removeAllValuesOf(properties: Array<out BindablePropertyBase<T>>) {
        properties.forEach {
            remove(it)
        }
    }

    fun removeAll(vararg properties: BindablePropertyBase<T>) = removeAllValuesOf(properties)
}

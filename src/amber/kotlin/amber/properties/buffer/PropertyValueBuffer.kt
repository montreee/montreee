package amber.properties.buffer

import amber.collections.moveAllValues
import amber.properties.value.PropertyValue

internal class PropertyValueBuffer<T> : ArrayList<PropertyValue<T>>() {
    val values: ArrayList<PropertyValue<T>>
        get() {
            val valuesToSet = ArrayList<PropertyValue<T>>()
            moveAllValues(valuesToSet)
            return valuesToSet
        }
}

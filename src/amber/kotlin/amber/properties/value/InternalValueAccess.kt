package amber.properties.value

class InternalValueAccess<T>(private val setLambda: (PropertyValue<T>) -> Unit) {
    operator fun invoke(value: PropertyValue<T>) {
        setLambda(value)
    }

    fun setInternalValue(value: PropertyValue<T>) {
        setLambda(value)
    }
}

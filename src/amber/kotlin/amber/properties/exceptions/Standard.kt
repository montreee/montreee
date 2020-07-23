package amber.properties.exceptions

open class PropertyException(message: String = "") : Exception(message)

class NeverAddedPropertyException : PropertyException("PropertyEventListener was never added to a property.")

class IllegalPropertyListenerAddPropertyException : PropertyException("PropertyEventListener is added on the wrong property.")

class IllegalBindPropertyException : PropertyException("Not allowed to bind.")

class AlreadyBoundPropertyException : PropertyException("Property is already bound.")

class IllegalSetPropertyException : PropertyException("Is not allowed to set the value of a bound property.")

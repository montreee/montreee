package amber.event

class ListenerList<T>(private val impl: MutableList<Listener<T>> = mutableListOf()) : MutableList<Listener<T>> by impl

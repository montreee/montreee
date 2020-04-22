package amber.event

interface Listener<T> {
    operator fun invoke(data: T) = data.on()
    fun T.on()
}

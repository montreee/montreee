package amber.event

open class Executor<T> {
    open fun execute(data: T, listeners: List<Listener<T>>) {
        listeners.forEach {
            it(data)
        }
    }
}

package amber.factory

interface Factory<T> {
    operator fun invoke(): T
}

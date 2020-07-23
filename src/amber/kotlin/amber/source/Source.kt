package amber.source

interface Source<T> {
    fun read(): T
}

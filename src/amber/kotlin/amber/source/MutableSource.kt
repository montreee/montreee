package amber.source

interface MutableSource<T> : Source<T> {
    fun write(t: T)
}

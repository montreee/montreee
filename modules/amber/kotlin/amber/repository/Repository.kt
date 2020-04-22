package amber.repository

interface Repository<T> : Iterable<T> {
    fun put(element: T)
    fun contains(element: T): Boolean
    fun remove(element: T)
    val elements: List<T>
}

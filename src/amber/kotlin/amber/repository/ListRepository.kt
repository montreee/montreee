package amber.repository

import amber.collections.sync
import amber.sync.Synchronized

class ListRepository<T>(
        private var list: MutableList<T> = mutableListOf()
) : Repository<T>, Synchronized by Synchronized() {

    init {
        list = list.sync()
    }

    override fun put(element: T) {
        list.add(element)
    }

    override fun contains(element: T): Boolean {
        return list.contains(element)
    }

    override fun remove(element: T) {
        list.remove(element)
    }

    override val elements: List<T>
        get() = list.toList()

    override fun iterator(): Iterator<T> = list.iterator()
}

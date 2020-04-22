package amber.repository

class RepositoryGroup<T>(vararg val items: Repository<T>) : Repository<T> {

    override fun put(element: T) {
        items.first().put(element)
    }

    override fun contains(element: T): Boolean {
        items.forEach {
            if (it.contains(element)) return@contains true
        }
        return false
    }

    override fun remove(element: T) {
        items.forEach {
            if (it.contains(element)) it.remove(element)
        }
    }

    override val elements: List<T>
        get() = run {
            val ret = mutableListOf<T>()
            items.forEach { it.elements.mapTo(ret) { it } }
            return@run ret
        }

    override fun iterator(): Iterator<T> {
        return elements.listIterator()
    }
}

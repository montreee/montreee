package amber.collections

import amber.sync.Synchronized

class SyncMutableList<T>(
        private val impl: MutableList<T> = mutableListOf(), synchronized: Synchronized = Synchronized()
) : MutableList<T>, Synchronized by synchronized {

    override val size: Int
        get() = synchronized { impl.size }

    override fun contains(element: T) = synchronized { impl.contains(element) }

    override fun containsAll(elements: Collection<T>) = synchronized { impl.containsAll(elements) }

    override fun get(index: Int) = synchronized { impl.get(index) }

    override fun indexOf(element: T) = synchronized { impl.indexOf(element) }

    override fun isEmpty() = synchronized { impl.isEmpty() }

    override fun iterator() = synchronized { impl.iterator() }

    override fun lastIndexOf(element: T) = synchronized { impl.lastIndexOf(element) }

    override fun add(element: T) = synchronized { impl.add(element) }

    override fun add(index: Int, element: T) = synchronized { impl.add(index, element) }

    override fun addAll(index: Int, elements: Collection<T>) = synchronized { impl.addAll(index, elements) }

    override fun addAll(elements: Collection<T>) = synchronized { impl.addAll(elements) }

    override fun clear() = synchronized { impl.clear() }

    override fun listIterator() = synchronized { impl.listIterator() }

    override fun listIterator(index: Int) = synchronized { impl.listIterator(index) }

    override fun remove(element: T) = synchronized { impl.remove(element) }

    override fun removeAll(elements: Collection<T>) = synchronized { impl.removeAll(elements) }

    override fun removeAt(index: Int) = synchronized { impl.removeAt(index) }

    override fun retainAll(elements: Collection<T>) = synchronized { impl.removeAll(elements) }

    override fun set(index: Int, element: T) = synchronized { impl.set(index, element) }

    override fun subList(fromIndex: Int, toIndex: Int) = synchronized { impl.subList(fromIndex, toIndex) }

}

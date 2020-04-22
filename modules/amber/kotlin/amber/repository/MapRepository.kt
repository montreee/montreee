package amber.repository

import amber.sync.Synchronized

open class MapRepository<K, V>(
        private val map: MutableMap<K, V> = mutableMapOf()
) : Repository<Map.Entry<K, V>>, Synchronized by Synchronized() {


    override val elements: List<Map.Entry<K, V>>
        get() = synchronized {
            map.entries.toList()
        }

    override fun contains(element: Map.Entry<K, V>) = synchronized {
        map.contains(element.key) && map.containsValue(element.value)
    }

    fun containsKey(key: K) = synchronized {
        map.contains(key)
    }

    override fun remove(element: Map.Entry<K, V>): Unit = synchronized {
        map.remove(element.key)
    }

    open fun removeKey(key: K) {
        synchronized {
            map.remove(key)
        }
    }

    open fun remove(key: K, value: V) {
        synchronized {
            map.remove(key, value)
        }
    }

    override fun put(element: Map.Entry<K, V>) = synchronized {
        map[element.key] = element.value
    }

    open fun put(key: K, value: V) {
        synchronized {
            map.put(key, value)
        }
    }

    fun get(key: K) = synchronized {
        map[key]
    }

    override fun iterator(): Iterator<Map.Entry<K, V>> = synchronized {
        elements.listIterator()
    }

}

package montreee.data

open class DataStore<V>(internal val impl: DataRepository<V> = DataRepository()) : Iterable<Map.Entry<String, V>> {

    override fun iterator() = impl.iterator()

    operator fun get(key: String) = impl.get(key)

    operator fun set(key: String, value: V) = impl.put(key, value)

    fun remove(key: String) = impl.removeKey(key)

    fun contains(key: String) = impl.containsKey(key)

}

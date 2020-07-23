package amber.mapquery

import amber.collections.containsIndex

open class MapQueryValue(
        value: Any,
        protected val parent: MapQueryValueParent? = null,
        createList: (() -> MutableList<*>)? = null,
        createMap: (() -> MutableMap<*, *>)? = null
) {

    open val value: Any by lazy { value }

    protected val createList: () -> MutableList<*> = createList ?: { mutableListOf<Any>() }
    protected val createMap: () -> MutableMap<*, *> = createMap ?: { mutableMapOf<Any, Any>() }

    operator fun get(key: Any): MapQueryValue {
        return if (isMap()) {
            if (contains(key)) MapQueryValue(map[key]!!, MapQueryValueParent(this))
            else placeHolderWithMapDefault(key)
        } else if (isList()) {
            if (key is Number) {
                if (contains(key)) MapQueryValue(list[key.toInt()], MapQueryValueParent(this))
                else placeHolderWithListDefault(key)
            } else throw MapQueryExceptionUseNumericKeyForList()
        } else throw MapQueryExceptionIllegalGet()
    }

    operator fun set(key: Any, value: Any) {
        if (isMutableMap()) mmap[key] = value
        else if (isMutableList()) {
            if (key is Number) {
                if (!mlist.containsIndex(key.toInt())) {
                    if (mlist.size == key) mlist.add(value)
                    else throw MapQueryExceptionOutOfListRangeException()
                } else mlist[key.toInt()] = value
            } else throw MapQueryExceptionUseNumericKeyForList()
        }
    }

    fun removeFromParent(recursively: Boolean = false) {
        if (hasParent()) {
            if (parent!!.obj.isMutableMap()) {
                val map = parent.obj.mmap
                map.entries.filter { it.value === value }.forEach { parent.obj.remove(it.key, recursively) }
            } else if (parent.obj.isMutableList()) {
                val list = parent.obj.mlist
                list.filter { it === value }.forEach { parent.obj.remove(list.indexOf(it), recursively) }
            }
        }
    }

    fun removeFromParentIfEmpty(recursively: Boolean = false) {
        if (isMap()) {
            if (map.isEmpty()) removeFromParent(recursively)
        } else if (isList()) if (list.isEmpty()) removeFromParent(recursively)
    }

    fun remove(key: Any, recursively: Boolean = false) {
        if (isMutableMap()) {
            if (contains(key)) mmap.remove(key)
        } else if (isMutableList()) if (key is Number) {
            if (contains(key)) mlist.removeAt(key.toInt())
        } else throw MapQueryExceptionUseNumericKeyForList()

        if (recursively) removeFromParentIfEmpty(recursively)
    }

    fun contains(key: Any): Boolean {
        if (isMap()) return map.contains(key)
        else if (isList()) if (key is Number) return list.containsIndex(key.toInt())
        else throw MapQueryExceptionUseNumericKeyForList()
        return false
    }

    private fun placeHolder(key: Any, defaultValue: Any): MapQueryValue {
        return MapQueryPlaceHolderValue(key, defaultValue, MapQueryValueParent(this))
    }

    private fun placeHolderWithMapDefault(key: Any, defaultValue: MutableMap<*, *> = createMap()): MapQueryValue {
        return placeHolder(key, defaultValue)
    }

    private fun placeHolderWithListDefault(key: Any, defaultValue: MutableList<*> = createList()): MapQueryValue {
        return placeHolder(key, defaultValue)
    }

    open fun useMap(): MapQueryValue {
        return MapQueryValue(map, parent)
    }

    open fun useList(): MapQueryValue {
        return MapQueryValue(list, parent)
    }

    private fun createMap(key: Any): MapQueryValue {
        val map = createMap()
        set(key, map)
        return MapQueryValue(map, MapQueryValueParent(this))
    }

    private fun createList(key: Any): MapQueryValue {
        val list = createList()
        set(key, list)
        return MapQueryValue(list, MapQueryValueParent(this))
    }

    val number get() = getAs<Number>()
    val byte get() = getAs<Byte>()
    val char get() = getAs<Char>()
    val double get() = getAs<Double>()
    val float get() = getAs<Float>()
    val int get() = getAs<Int>()
    val long get() = getAs<Long>()

    val short get() = getAs<Short>()

    val string get() = getAs<String>()
    val list get() = getAs<List<Any>>()

    val mlist get() = getAs<MutableList<Any>>()
    val map get() = getAs<Map<Any, Any>>()

    val mmap get() = getAs<MutableMap<Any, Any>>()
    val numberOrNull get() = getAsOrNull<Number>()
    val byteOrNull get() = getAsOrNull<Byte>()
    val charOrNull get() = getAsOrNull<Char>()
    val doubleOrNull get() = getAsOrNull<Double>()
    val floatOrNull get() = getAsOrNull<Float>()
    val intOrNull get() = getAsOrNull<Int>()
    val longOrNull get() = getAsOrNull<Long>()

    val shortOrNull get() = getAsOrNull<Short>()

    val stringOrNull get() = getAsOrNull<String>()
    val listOrNull get() = getAsOrNull<List<Any>>()

    val mlistOrNull get() = getAsOrNull<MutableList<Any>>()
    val mapOrNull get() = getAsOrNull<Map<Any, Any>>()

    val mmapOrNull get() = getAsOrNull<MutableMap<Any, Any>>()

    inline fun <reified T> getAs(): T {
        return getAsOrNull<T>() ?: throw MapQueryExceptionValueCantBeCastedTo(
                value::class.qualifiedName!!, T::class.qualifiedName!!
        )
    }

    inline fun <reified T> getAsOrNull(): T? {
        return if (value is T) value as T
        else null
    }

    fun isNumber() = value is Number
    fun isByte() = value is Byte
    fun isChar() = value is Char
    fun isDouble() = value is Double
    fun isFloat() = value is Float
    fun isInt() = value is Int
    fun isLong() = value is Long
    fun isShort() = value is Short

    fun isString() = value is String

    fun isList() = value is List<*>
    fun isMutableList() = value is MutableList<*>

    fun isMap() = value is Map<*, *>

    fun isMutableMap() = value is MutableMap<*, *>

    private fun hasParent() = parent != null
}

package amber.mapquery

class MapQueryPlaceHolderValue(
        val key: Any,
        defaultValue: Any,
        parent: MapQueryValueParent,
        createList: (() -> MutableList<*>)? = null,
        createMap: (() -> MutableMap<*, *>)? = null
) : MapQueryValue(defaultValue, parent, createList, createMap) {

    private var replaceWith: Any? = null

    @Volatile
    var isFinal = false
    override val value: Any by lazy {
        isFinal = true
        val ret = replaceWith ?: defaultValue
        this.parent?.obj?.set(key, ret)
        ret
    }

    fun replaceWith(value: Any) {
        if (isFinal) return

        replaceWith = value
    }

    override fun useMap(): MapQueryValue {
        replaceWith(createMap())
        return MapQueryValue(map, parent)
    }

    override fun useList(): MapQueryValue {
        replaceWith(createList())
        return MapQueryValue(list, parent)
    }
}

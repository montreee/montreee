package de.contentup.montreee.modules.webui.repository

class Path(val value: String) {

    constructor(trace: List<String>) : this(trace.joinToString("/"))
    constructor(vararg trace: String) : this(trace.asList())
    constructor(parents: List<String>, element: String) : this(*parents.toTypedArray(), element)
    constructor(vararg parent: String, element: String) : this(parent.asList(), element)

    val trace = value.split("/")
    val element = trace.last()
    val parents = trace.filter { it !== element }

    val parent get() = Path(parents)

    fun isChild(parent: Path) = parent.isParent(this)
    fun isParent(child: Path): Boolean {
        if (child.trace.size <= trace.size) return false
        if (child.trace.subList(0, trace.size - 1) != trace) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Path) return false
        if (other.trace != trace) return false
        return true
    }

    override fun toString() = value

}

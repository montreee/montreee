package de.contentup.montreee.modules.webui.repository.treee

class Path(val value: String) {
    constructor(trace: List<String>) : this(trace.joinToString("/"))
    constructor(vararg trace: String) : this(trace.asList())
    constructor(parents: List<String>, element: String) : this(*parents.toTypedArray(), element)
    constructor(vararg parent: String, element: String) : this(parent.asList(), element)

    val trace = value.split("/")
    val parents = trace.takeWhile { it !== element }
    val element = trace.last()
}

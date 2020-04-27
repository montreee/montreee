package de.contentup.montreee.modules.webui.repository.treee.impl

import amber.collections.sync
import de.contentup.montreee.modules.webui.repository.treee.Element
import de.contentup.montreee.modules.webui.repository.treee.Path
import de.contentup.montreee.modules.webui.repository.treee.Repository

class InMemoryListRepository(list: MutableList<Element> = mutableListOf()) : Repository {
    private val list = list.sync()
    override fun find(path: Path): Element? = list.synchronized ret@{
        return@ret list.find { it.path == path }
    }

    override fun childes(path: Path): List<Element> = list.synchronized ret@{
        return@ret list.filter { it.path.parent == path }
    }

    override fun delete(path: Path): Element? = list.synchronized ret@{
        val element = find(path)
        if (!list.remove(element)) return@ret null
        list.removeIf { path.isParent(it.path) }
        return@ret element
    }

    override fun insert(element: Element): Path? = list.synchronized ret@{
        if (find(element.path) != null) return@ret null
        find(element.path.parent) ?: return@ret null
        list.add(element)
        return@ret element.path
    }
}

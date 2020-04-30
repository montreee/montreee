package de.contentup.montreee.modules.webui.repository.impl

import amber.collections.sync
import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.Path
import de.contentup.montreee.modules.webui.repository.Repository

class InMemoryListRepository(list: MutableList<Element> = mutableListOf()) : Repository {

    private val list = list.sync()

    override fun find(path: Path): Element? = list.synchronized func@{
        if (path.value.isBlank()) return@func Element.Folder(Path(""))
        list.find { it.path == path }
    }

    override fun childes(path: Path): List<Element> = list.synchronized {
        list.filter { it.path.parent == path }
    }

    override fun delete(path: Path): Element? = list.synchronized func@{
        val element = find(path)
        if (!list.remove(element)) return@func null
        list.removeIf { path.isParent(it.path) }
        return@func element
    }

    override fun insert(element: Element): Path? = list.synchronized func@{
        if (find(element.path) != null) return@func null
        find(element.path.parent) ?: return@func null
        list.add(element)
        return@func element.path
    }

}

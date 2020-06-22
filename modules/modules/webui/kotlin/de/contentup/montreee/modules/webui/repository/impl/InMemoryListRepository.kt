package de.contentup.montreee.modules.webui.repository.impl

import amber.collections.copyAllValues
import amber.collections.sync
import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.Path
import de.contentup.montreee.modules.webui.repository.Repository

class InMemoryListRepository(list: MutableList<Element> = mutableListOf()) : Repository {

    private val list = list.sync()

    override fun find(path: Path): Element? = list.synchronized { internalFind(path) }
    override fun childes(path: Path, depth: Int): List<Element> = list.synchronized { internalChildes(path, depth) }
    override fun delete(path: Path): Element? = list.synchronized { internalDelete(path) }
    override fun insert(element: Element): Path? = list.synchronized { internalInsert(element) }
    override fun move(from: Path, to: Path): Element? = list.synchronized { internalMove(from, to) }

    private fun internalFind(path: Path): Element? {
        if (path.value.isBlank()) return Element.Folder(Path(""))
        return list.find { it.path == path }
    }

    private fun internalChildes(path: Path, depth: Int = 0): List<Element> {
        if (path.trace.isEmpty()) {
            if (depth == 0) return mutableListOf<Element>().apply { copyAllValues(list) }
            return list.filter { it.path.trace.size <= depth }
        }

        val element = internalFind(path) ?: return emptyList()
        val childes = mutableListOf<Element>()
        list.forEach {
            if (element.path.trace.size >= it.path.trace.size) return@forEach
            if (depth != 0 && it.path.trace.size - element.path.trace.size > depth) return@forEach
            element.path.trace.forEachIndexed { i, e ->
                if (e != it.path.trace[i]) return@forEach
            }
            childes.add(it)
        }
        return childes
    }

    private fun internalDelete(path: Path): Element? {
        val element = internalFind(path) ?: return null
        val childes = internalChildes(path)
        if (!list.remove(element)) return null
        list.removeAll(childes)
        return element
    }

    private fun internalInsert(element: Element): Path? {
        if (internalFind(element.path) != null) return null
        if (element.path.parent != null) internalFind(element.path.parent!!) ?: return null
        list.add(element)
        return element.path
    }

    private fun internalMove(from: Path, to: Path): Element? {
        fun moveElement(from: Path, to: Path) {
            val element = internalFind(from) ?: return
            element.apply { path = to }
        }

        val element = internalFind(from)
        if (element == null || internalFind(to) != null) return null
        childes(from).forEach {
            moveElement(it.path, Path(to.trace + it.path.trace.subList(from.trace.size, it.path.trace.size)))
        }
        moveElement(from, to)
        return internalFind(to)
    }
}

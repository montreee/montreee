package de.contentup.montreee.modules.webui.repository.treee

interface Repository {
    fun find(path: Path): Element?
    fun delete(path: Path): Element?
    fun insert(element: Element): Path?
}

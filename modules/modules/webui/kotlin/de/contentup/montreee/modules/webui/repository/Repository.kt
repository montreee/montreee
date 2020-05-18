package de.contentup.montreee.modules.webui.repository

interface Repository {
    fun find(path: Path): Element?
    fun childes(path: Path, depth: Int = 0): List<Element>
    fun delete(path: Path): Element?
    fun insert(element: Element): Path?
    fun move(from: Path, to: Path): Element?
}

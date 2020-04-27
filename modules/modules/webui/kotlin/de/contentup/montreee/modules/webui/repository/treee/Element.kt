package de.contentup.montreee.modules.webui.repository.treee

sealed class Element(val path: Path) {
    class Folder(path: Path) : Element(path)
    class Module(path: Path, val type: String) : Element(path)
    class Parameter(path: Path, val type: String) : Element(path)
}

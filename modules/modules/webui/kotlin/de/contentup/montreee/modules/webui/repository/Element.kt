package de.contentup.montreee.modules.webui.repository

sealed class Element(var path: Path) {
    class Folder(path: Path) : Element(path)
    class Module(path: Path, val type: String) : Element(path)
    class Parameter(path: Path, val type: String) : Element(path)
}

package de.contentup.montreee.modules.webui

import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.Path

object TestData {
    val list
        get() = mutableListOf<Element>().apply {
            fun add(maxLevel: Int, level: Int = 1, currentPath: String = "") {
                if (level < maxLevel) {
                    repeat(5) {
                        add(Element.Folder(Path(currentPath + "folder$it")))
                        add(maxLevel, level + 1, currentPath + "folder$it/")
                    }
                }
                repeat(5) {
                    add(Element.Module(Path(currentPath + "module$it"), "TestModule"))
                }
            }
            add(8)
        }
}

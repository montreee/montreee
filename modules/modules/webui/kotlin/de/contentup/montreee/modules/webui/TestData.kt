package de.contentup.montreee.modules.webui

import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.Path
import de.contentup.montreee.modules.webui.repository.types.Folder
import de.contentup.montreee.modules.webui.repository.types.Module

object TestData {
    val list
        get() = mutableListOf<Element>().apply {
            fun add(maxLevel: Int, level: Int = 1, currentPath: String = "") {
                if (level < maxLevel) {
                    repeat(5) {
                        add(Element(Path(currentPath + "folder$it"), Folder()))
                        add(maxLevel, level + 1, currentPath + "folder$it/")
                    }
                }
                repeat(5) {
                    add(Element(Path(currentPath + "module$it"), Module("TestModule")))
                }
            }
            add(8)
        }
}

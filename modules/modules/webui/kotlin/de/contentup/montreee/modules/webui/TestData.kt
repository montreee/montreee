package de.contentup.montreee.modules.webui

import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.Path
import de.contentup.montreee.modules.webui.repository.impl.InMemoryListRepository

object TestData {
    val list = mutableListOf<Element>().apply {
        InMemoryListRepository(this).apply {
            fun add(maxLevel: Int, level: Int = 1, currentPath: String = "") {
                if (level < maxLevel) {
                    repeat(5) {
                        insert(Element.Folder(Path(currentPath + "folder$it")))
                        add(maxLevel, level + 1, currentPath + "folder$it/")
                    }
                }
                repeat(5) {
                    insert(Element.Module(Path(currentPath + "module$it"), "TestModule"))
                }
            }
            add(5)
        }
    }
}

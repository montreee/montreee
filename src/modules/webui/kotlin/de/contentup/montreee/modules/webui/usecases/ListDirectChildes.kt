package de.contentup.montreee.modules.webui.usecases

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.Path

class ListDirectChildes(val context: ApplicationContext) {

    operator fun invoke(path: String): List<Element> {
        return context.repository.childes(Path(path), 1)
    }
}

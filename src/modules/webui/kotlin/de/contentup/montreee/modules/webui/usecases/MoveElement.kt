package de.contentup.montreee.modules.webui.usecases

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.repository.Path

class MoveElement(val context: ApplicationContext) {

    operator fun invoke(from: String, to: String) {
        context.repository.move(Path(from), Path(to))
    }
}

package de.contentup.montreee.modules.webui.usecases

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.repository.Path

class DeleteElement(val context: ApplicationContext) {

    operator fun invoke(path: String) {
        context.repository.delete(Path(path))
    }
}

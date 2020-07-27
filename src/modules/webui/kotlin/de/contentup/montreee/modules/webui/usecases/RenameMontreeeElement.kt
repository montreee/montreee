package de.contentup.montreee.modules.webui.usecases

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.repository.Path

class RenameMontreeeElement(val context: ApplicationContext) {

    operator fun invoke(path: String, name: String) {
        val current = Path(path)
        val new = Path(current.trace.dropLast(1).toMutableList().apply { add(name) })
        context.repository.move(current, new)
    }
}

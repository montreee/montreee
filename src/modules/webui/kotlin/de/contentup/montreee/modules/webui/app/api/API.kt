package de.contentup.montreee.modules.webui.app.api

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.api.tree.tree
import io.ktor.routing.*

fun Route.api(context: ApplicationContext) {
    route("tree") { tree(context) }
}

package de.contentup.montreee.modules.webui.app.api.tree

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.api.tree.edit.edit
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.tree(context: ApplicationContext) {
    route("edit") { edit(context) }
}

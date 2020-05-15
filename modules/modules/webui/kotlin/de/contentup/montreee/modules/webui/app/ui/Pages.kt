package de.contentup.montreee.modules.webui.app.ui

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.ui.pages.index
import de.contentup.montreee.modules.webui.app.ui.pages.status
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.pages(context: ApplicationContext) {
    get("/") { index(context) }
    route("views") { views(context) }
    status(context)
}

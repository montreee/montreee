package de.contentup.montreee.modules.webui.app.ui

import de.contentup.montreee.modules.webui.app.ApplicationContext
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.ui(context: ApplicationContext) {
    pages(context)
    get("/{path...}") { fallback(context) }
}

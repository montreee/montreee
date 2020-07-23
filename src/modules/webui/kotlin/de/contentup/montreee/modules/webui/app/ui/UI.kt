package de.contentup.montreee.modules.webui.app.ui

import de.contentup.montreee.modules.webui.app.ApplicationContext
import io.ktor.routing.*

fun Route.ui(context: ApplicationContext) {
    pages(context)
    get("/{path...}") { fallback(context) }
}

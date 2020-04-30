package de.contentup.montreee.modules.webui.app

import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.routes(context: ApplicationContext) {
    pages(context)
    status(context)
    get("{...}") { fallback(context) }
}

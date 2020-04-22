package de.contentup.montreee.modules.webui.app

import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.routes() {
    pages()
    status()
    get("{...}") { fallback() }
}

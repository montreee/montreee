package de.contentup.montreee.modules.webui.app

import de.contentup.montreee.modules.webui.app.page.index
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

fun Routing.pages(context: ApplicationContext) {
    get("/") { index(context) }
    route("views") { views(context) }
}

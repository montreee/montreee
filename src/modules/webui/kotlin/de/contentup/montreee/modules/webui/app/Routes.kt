package de.contentup.montreee.modules.webui.app

import de.contentup.montreee.modules.webui.app.api.api
import de.contentup.montreee.modules.webui.app.ui.ui
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.routes(context: ApplicationContext) {
    //todo fix
    get("/scss/{path...}") {
        call.respondRedirect("/ui/scss/${call.parameters.getAll("path")?.joinToString("/")}", true)
    }

    route("/api") { api(context) }
    route("/ui") { ui(context) }
    get {
        call.respondRedirect("/ui", true)
    }
}

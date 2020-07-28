package de.contentup.montreee.modules.webui.app.api.tree.edit

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.usecases.DeleteElement
import de.contentup.montreee.modules.webui.usecases.MoveElement
import de.contentup.montreee.modules.webui.usecases.RenameElement
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.edit(context: ApplicationContext) {
    delete("delete") {
        val path = call.parameters["path"] ?: return@delete
        DeleteElement(context)(path)
        call.respond(HttpStatusCode.OK)
    }
    put("move") {
        val from = call.parameters["from"] ?: return@put
        val to = call.parameters["to"] ?: return@put
        MoveElement(context)(from, to)
        call.respond(HttpStatusCode.OK)
    }
    put("rename") {
        val path = call.parameters["path"] ?: return@put
        val name = call.parameters["name"] ?: return@put
        RenameElement(context)(path, name)
        call.respond(HttpStatusCode.OK)
    }
}

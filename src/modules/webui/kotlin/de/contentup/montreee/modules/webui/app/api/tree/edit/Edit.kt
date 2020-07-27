package de.contentup.montreee.modules.webui.app.api.tree.edit

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.usecases.DeleteMontreeeElement
import de.contentup.montreee.modules.webui.usecases.MoveMontreeeElement
import de.contentup.montreee.modules.webui.usecases.RenameMontreeeElement
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.edit(context: ApplicationContext) {
    delete("delete") {
        val path = call.parameters["path"] ?: return@delete
        DeleteMontreeeElement(context)(path)
        call.respond(HttpStatusCode.OK)
    }
    put("move") {
        val from = call.parameters["from"] ?: return@put
        val to = call.parameters["to"] ?: return@put
        MoveMontreeeElement(context)(from, to)
        call.respond(HttpStatusCode.OK)
    }
    put("rename") {
        val path = call.parameters["path"] ?: return@put
        val name = call.parameters["name"] ?: return@put
        RenameMontreeeElement(context)(path, name)
        call.respond(HttpStatusCode.OK)
    }
}

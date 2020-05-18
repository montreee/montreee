package de.contentup.montreee.modules.webui.app.api.tree.edit

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.usecases.DeleteMontreeeElement
import de.contentup.montreee.modules.webui.usecases.MoveMontreeeElement
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.put

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
}

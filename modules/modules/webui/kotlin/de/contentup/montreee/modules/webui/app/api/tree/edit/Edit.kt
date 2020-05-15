package de.contentup.montreee.modules.webui.app.api.tree.edit

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.usecases.DeleteMontreeeElement
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete

fun Route.edit(context: ApplicationContext) {
    delete {
        val path = call.parameters["path"] ?: return@delete

        DeleteMontreeeElement(context)(path)

        call.respond(HttpStatusCode.OK)
    }
}

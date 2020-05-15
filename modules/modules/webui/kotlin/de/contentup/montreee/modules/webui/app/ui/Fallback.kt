package de.contentup.montreee.modules.webui.app.ui

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.webuiDataFolder
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respondFile
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.fallback(context: ApplicationContext) {
    val path = call.parameters.getAll("path")?.joinToString("/") ?: return
    val safePath = if (path.startsWith("/")) path.removePrefix("/") else path
    val file = webuiDataFolder.resolve(safePath)

    if (!file.isFile) return

    call.respondFile(file)
}

package de.contentup.montreee.modules.webui.app

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.path
import io.ktor.response.respondFile
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.fallback() {
    val path = call.request.path()
    val safePath = if (path.startsWith("/")) path.removePrefix("/") else path
    val file = webuiDataFolder.resolve(safePath)

    if (!file.isFile) return

    call.respondFile(file)
}

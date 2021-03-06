package de.contentup.montreee.modules.webui.app.ui.util

import de.contentup.montreee.Application
import de.contentup.montreee.module.module
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.SectionCommentTagConsumer
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.sectionCommentsConsumer
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import java.io.BufferedWriter

suspend fun ApplicationCall.respondRawHtmlWithSectionComments(
        sectionCommentsEnabled: Boolean = Application.module.config.development,
        status: HttpStatusCode = HttpStatusCode.OK,
        block: SectionCommentTagConsumer<BufferedWriter>.() -> Unit = {}
) {
    respond(RawHtmlContent(status) {
        sectionCommentsConsumer(sectionCommentsEnabled, block)
    })
}
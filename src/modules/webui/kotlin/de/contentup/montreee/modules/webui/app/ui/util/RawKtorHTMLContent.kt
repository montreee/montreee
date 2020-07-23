package de.contentup.montreee.modules.webui.app.ui.util

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.html.TagConsumer
import kotlinx.html.stream.appendHTML
import java.io.BufferedWriter

suspend fun ApplicationCall.respondRawHtml(
        status: HttpStatusCode = HttpStatusCode.OK, block: TagConsumer<BufferedWriter>.() -> Unit
) {
    respond(RawHtmlContent(status, block))
}

class RawHtmlContent(
        override val status: HttpStatusCode? = null, private val builder: TagConsumer<BufferedWriter>.() -> Unit
) : OutgoingContent.WriteChannelContent() {

    override val contentType: ContentType
        get() = ContentType.Text.Html.withCharset(Charsets.UTF_8)

    @KtorExperimentalAPI
    override suspend fun writeTo(channel: ByteWriteChannel) {
        channel.bufferedWriter().use {
            it.appendHTML().apply(builder)
        }
    }
}

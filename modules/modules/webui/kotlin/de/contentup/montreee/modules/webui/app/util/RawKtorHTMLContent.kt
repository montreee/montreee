package de.contentup.montreee.modules.webui.app.util

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.withCharset
import io.ktor.response.respond
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.cio.bufferedWriter
import io.ktor.utils.io.ByteWriteChannel
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

package de.contentup.montreee.modules.webui.app.ui.pages

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.ui.StaticLinks
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.comment
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags.html5Doctype
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags.script
import de.contentup.montreee.modules.webui.app.ui.util.respondRawHtmlWithSectionComments
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.*

data class HttpStatusInformation(val code: Int, val name: String, val description: String = "")

val httpStatusInformation
    get() = mutableListOf(
            HttpStatusInformation(100, "Continue"),
            HttpStatusInformation(101, "Switching Protocols"),
            HttpStatusInformation(102, "Processing"),
            HttpStatusInformation(200, "OK"),
            HttpStatusInformation(201, "Created"),
            HttpStatusInformation(202, "Accepted"),
            HttpStatusInformation(203, "Non-authoritative Information"),
            HttpStatusInformation(204, "No Content"),
            HttpStatusInformation(205, "Reset Content"),
            HttpStatusInformation(206, "Partial Content"),
            HttpStatusInformation(207, "Multi-Status"),
            HttpStatusInformation(208, "Already Reported"),
            HttpStatusInformation(226, "IM Used"),
            HttpStatusInformation(300, "Multiple Choices"),
            HttpStatusInformation(301, "Moved Permanently"),
            HttpStatusInformation(302, "Found"),
            HttpStatusInformation(303, "See Other"),
            HttpStatusInformation(304, "Not Modified"),
            HttpStatusInformation(305, "Use Proxy"),
            HttpStatusInformation(307, "Temporary Redirect"),
            HttpStatusInformation(308, "Permanent Redirect"),
            HttpStatusInformation(400, "Bad Request"),
            HttpStatusInformation(401, "Unauthorized"),
            HttpStatusInformation(402, "Payment Required"),
            HttpStatusInformation(403, "Forbidden"),
            HttpStatusInformation(404, "Not Found"),
            HttpStatusInformation(405, "Method Not Allowed"),
            HttpStatusInformation(406, "Not Acceptable"),
            HttpStatusInformation(407, "Proxy Authentication Required"),
            HttpStatusInformation(408, "Request Timeout"),
            HttpStatusInformation(409, "Conflict"),
            HttpStatusInformation(410, "Gone"),
            HttpStatusInformation(411, "Length Required"),
            HttpStatusInformation(412, "Precondition Failed"),
            HttpStatusInformation(413, "Payload Too Large"),
            HttpStatusInformation(414, "Request-URI Too Long"),
            HttpStatusInformation(415, "Unsupported Media Type"),
            HttpStatusInformation(416, "Requested Range Not Satisfiable"),
            HttpStatusInformation(417, "Expectation Failed"),
            HttpStatusInformation(418, "I'm a teapot"),
            HttpStatusInformation(421, "Misdirected Request"),
            HttpStatusInformation(422, "Unprocessable Entity"),
            HttpStatusInformation(423, "Locked"),
            HttpStatusInformation(424, "Failed Dependency"),
            HttpStatusInformation(426, "Upgrade Required"),
            HttpStatusInformation(428, "Precondition Required"),
            HttpStatusInformation(429, "Too Many Requests"),
            HttpStatusInformation(431, "Request Header Fields Too Large"),
            HttpStatusInformation(444, "Connection Closed Without Response"),
            HttpStatusInformation(451, "Unavailable For Legal Reasons"),
            HttpStatusInformation(499, "Client Closed Request"),
            HttpStatusInformation(500, "Internal Server Error"),
            HttpStatusInformation(501, "Not Implemented"),
            HttpStatusInformation(502, "Bad Gateway"),
            HttpStatusInformation(503, "Service Unavailable"),
            HttpStatusInformation(504, "Gateway Timeout"),
            HttpStatusInformation(505, "HTTP Version Not Supported"),
            HttpStatusInformation(506, "Variant Also Negotiates"),
            HttpStatusInformation(507, "Insufficient Storage"),
            HttpStatusInformation(508, "Loop Detected"),
            HttpStatusInformation(510, "Not Extended"),
            HttpStatusInformation(511, "Network Authentication Required"),
            HttpStatusInformation(599, "Network Connect Timeout Error")
    )

fun Application.installUIStatusPages() {
    install(StatusPages) {
        val handledStatusCodes = mutableListOf<HttpStatusCode>().apply {
            httpStatusInformation.forEach { add(HttpStatusCode.fromValue(it.code)) }
        }.toTypedArray()
        status(*handledStatusCodes) {
            if (!call.request.local.uri.startsWith("ui")) return@status
            if (it.isSuccess()) return@status
            if (it.toString().startsWith("3")) return@status

            call.respondRedirect("/${it.value}")
        }
    }
}

fun Route.status(context: ApplicationContext) {
    httpStatusInformation.forEach { status ->
        get(status.code.toString()) {
            respondHtmlStatusCodePage(status)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondHtmlStatusCodePage(
        httpStatusInformation: HttpStatusInformation
) {
    call.respondRawHtmlWithSectionComments {
        html5Doctype()

        html {
            lang = "en"

            head {

                comment("metadata") {
                    base {
                        href = "./"
                    }
                    meta {
                        charset = "utf-8"
                    }
                    meta {
                        attributes["http-equiv"] = "X-UA-Compatible"
                        content = "IE=edge"
                    }
                    meta {
                        name = "viewport"
                        content = "width=device-width, initial-scale=1.0, shrink-to-fit=no"
                    }
                    meta {
                        name = "description"
                        content = "Status ${httpStatusInformation.code}"
                    }
                }

                title { +"Status ${httpStatusInformation.code}" }

                comment("styles sheets") {
                    styleLink(StaticLinks.CSS.Pace)
                    styleLink(StaticLinks.CSS.Montreee)
                }
            }

            body(classes = "montreee-app montreee-dark-theme flex-row align-items-center") {
                div(classes = "container") {
                    div(classes = "row justify-content-center") {
                        div(classes = "col-md-6") {
                            div(classes = "clearfix") {
                                h1(classes = "float-left display-3 mr-4") {
                                    +httpStatusInformation.code.toString()
                                }
                                h4(classes = "pt-3") {
                                    +httpStatusInformation.name
                                }
                                if (httpStatusInformation.description.isBlank()) {
                                    a {
                                        //todo decide on removing
                                        href = "https://httpstatuses.com/${httpStatusInformation.code}"
                                        +"more information"
                                    }
                                } else {
                                    p(classes = "text-muted") {
                                        +httpStatusInformation.description
                                    }
                                }
                            }
                        }
                    }
                }
                script(src = StaticLinks.JS.Before)
                script(src = StaticLinks.JS.Lib.JQuery)
                script(src = StaticLinks.JS.Lib.CoreUI)
            }
        }
    }
}

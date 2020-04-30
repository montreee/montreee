package de.contentup.montreee.modules.webui.app.page.views

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.htmlDsl.comment
import de.contentup.montreee.modules.webui.app.util.respondRawHtmlWithSectionComments
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.*

object teststuff {
    fun resolve(path: String): Map<String, String> {
        val element = path.substringAfterLast("/")
        return if (element.startsWith("file")) mapOf()
        else mutableMapOf<String, String>().apply {
            repeat(5) {
                this["folder${it + 1}"] = "Folder"
            }
            repeat(5) {
                this["file${it + 1}"] = "File"
            }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.montreeeView(context: ApplicationContext) {
    val path = call.parameters.getAll("path")?.joinToString("/") ?: ""
    val fullPath = if (!path.isBlank()) "montreee/$path" else "montreee"
    val content = if (path.isBlank()) {
        teststuff.resolve("")
    } else {
        teststuff.resolve(path)
    }
    call.respondRawHtmlWithSectionComments {
        comment("view") {

            comment("dependencies") {

                comment("stylesheets") {

                }

                comment("scripts") {

                }

            }

            comment("location") {
                ol(classes = "breadcrumb") {
                    var currentItem = ""
                    var currentPath = ""
                    fullPath.forEach {
                        when (it) {
                            '/'  -> {
                                li {
                                    a(classes = "montreee-xhr-link") {
                                        href = currentPath
                                        +currentItem
                                    }
                                }
                                li(classes = "separator") {
                                    +it.toString()
                                }
                                currentItem = ""
                            }
                            else -> {
                                currentItem += it
                            }
                        }
                        currentPath += it
                    }
                    if (currentItem.isNotBlank()) {
                        li(classes = "font-weight-bold") {
                            +currentItem
                        }
                    }
                }
            }

            comment("content") {
                div(classes = "montreee-body ${if (path.isBlank()) "fade-in" else ""}") {
                    main(classes = "montreee-main") {
                        div(classes = "container-fluid") {
                            if (content.isNotEmpty()) {
                                div(classes = "card") {
                                    div(classes = "card-body") {
                                        content.forEach {
                                            div(classes = "row px-4 py-1") {
                                                when (it.value) {
                                                    "Folder", "File" -> {
                                                        a(classes = "montreee-xhr-link") {
                                                            href = "$fullPath/${it.key}"
                                                            +it.key
                                                        }
                                                    }
                                                    else             -> {
                                                        a(classes = "disabled") {
                                                            +it.key
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

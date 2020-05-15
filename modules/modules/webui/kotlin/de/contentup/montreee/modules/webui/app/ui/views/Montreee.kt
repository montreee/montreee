package de.contentup.montreee.modules.webui.app.ui.views

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.comment
import de.contentup.montreee.modules.webui.app.ui.util.respondRawHtmlWithSectionComments
import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.childes
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.montreeeView(context: ApplicationContext) {
    val path = call.parameters.getAll("path")?.joinToString("/") ?: ""

    val pathPrefix = "montreee"
    val content = context.repository.childes(path.ifBlank { "" })

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
                    (if (!path.isBlank()) "montreee/$path" else "montreee").forEach {
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
                                                when (it) {
                                                    is Element.Folder -> {
                                                        a(classes = "montreee-xhr-link") {
                                                            href = "$pathPrefix/${it.path}"
                                                            +it.path.element
                                                        }
                                                        button {
                                                            onClick = "montreeeCallTreeEditDeleteMethod(\"${it.path}\", \"${(if (!path.isBlank()) "montreee/$path" else "montreee")}\")"
                                                            +"delete"
                                                        }
                                                    }
                                                    else              -> {
                                                        a(classes = "disabled") {
                                                            +it.path.element
                                                        }
                                                        button {
                                                            onClick = "montreeeCallTreeEditDeleteMethod(\"${it.path}\", \"${(if (!path.isBlank()) "montreee/$path" else "montreee")}\")"
                                                            +"delete"
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

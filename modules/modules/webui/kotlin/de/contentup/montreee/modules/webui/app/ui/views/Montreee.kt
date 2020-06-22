package de.contentup.montreee.modules.webui.app.ui.views

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.ui.StaticLinks
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.comment
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags.script
import de.contentup.montreee.modules.webui.app.ui.util.respondRawHtmlWithSectionComments
import de.contentup.montreee.modules.webui.repository.Element
import de.contentup.montreee.modules.webui.repository.Path
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.montreeeView(context: ApplicationContext) {
    val path = call.parameters.getAll("path")?.joinToString("/") ?: ""

    val content = context.repository.childes(Path(path.ifBlank { "" }), 1)

    call.respondRawHtmlWithSectionComments {
        comment("view") {

            comment("dependencies") {

                comment("stylesheets") {

                }

                comment("scripts") {
                    script(src = StaticLinks.JS.ApiButtons)
                    script(src = StaticLinks.JS.DialogMoveElements)
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
                    comment("dialogs") {
                        comment("move element dialog") {
                            div(classes = "modal fade") {
                                id = "dialog-move-element"
                                role = "dialog"
                                attributes["tabindex"] = "-1"
                                attributes["aria-hidden"] = "true"
                                div(classes = "modal-dialog") {
                                    role = "document"
                                    div(classes = "modal-content") {
                                        div(classes = "modal-header") {
                                            h5(classes = "modal-title") {
                                                +"Move"
                                            }
                                            button(classes = "close") {
                                                type = ButtonType.button
                                                attributes["data-dismiss"] = "modal"
                                                attributes["aria-label"] = "Close"
                                                i(classes = "fa fa-times")
                                            }
                                        }
                                        div(classes = "modal-body") {
                                            p {
                                                id = "dialog-move-element-current-path"
                                            }
                                            input {
                                                id = "dialog-move-element-future-path"
                                            }
                                        }
                                        div(classes = "modal-footer") {
                                            button(classes = "btn btn-secondary") {
                                                type = ButtonType.button
                                                attributes["data-dismiss"] = "modal"
                                                +"Close"
                                            }
                                            button(classes = "montreee-api-button btn btn-primary") {
                                                id = "dialog-move-element-move-button"
                                                type = ButtonType.button
                                                attributes["data-method"] = "PUT"
                                                attributes["data-url"] = "api/tree/edit/move"
                                                attributes["data-load-after-view-url"] = if (!path.isBlank()) "montreee/$path" else "montreee"
                                                +"Move"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    comment("main") {
                        main(classes = "montreee-main") {
                            div(classes = "container-fluid") {
                                if (content.isNotEmpty()) {
                                    div(classes = "card") {
                                        div(classes = "card-body") {
                                            content.forEach {
                                                element(it, path)
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

private fun DIV.element(
        it: Element,
        path: String
) {
    div(classes = "row px-4 py-1") {
        when (it) {
            is Element.Folder -> {
                a(classes = "montreee-xhr-link") {
                    href = "montreee/${it.path}"
                    +(it.path.element ?: "")
                }
                button(classes = "montreee-api-button btn btn-secondary") {
                    attributes["data-method"] = "DELETE"
                    attributes["data-url"] = "api/tree/edit/delete"
                    attributes["data-load-after-view-url"] = if (!path.isBlank()) "montreee/$path" else "montreee"
                    attributes["data-parameter-path"] = "${it.path}"
                    +"delete"
                }
                button(classes = "btn btn-primary") {
                    type = ButtonType.button
                    attributes["data-toggle"] = "modal"
                    attributes["data-target"] = "#dialog-move-element"
                    onClick = "dialogMoveElement(\"${it.path}\")"
                    +"move"
                }
            }
            else              -> {
                a(classes = "disabled") {
                    +(it.path.element ?: "")
                }
                button(classes = "montreee-api-button btn btn-secondary") {
                    attributes["data-method"] = "DELETE"
                    attributes["data-url"] = "api/tree/edit/delete"
                    attributes["data-load-after-view-url"] = if (!path.isBlank()) "montreee/$path" else "montreee"
                    attributes["data-parameter-path"] = "${it.path}"
                    +"delete"
                }
                button(classes = "btn btn-primary") {
                    type = ButtonType.button
                    attributes["data-toggle"] = "modal"
                    attributes["data-target"] = "#dialog-move-element"
                    onClick = "dialogMoveElement(\"${it.path}\")"
                    +"move"
                }
            }
        }
    }
}

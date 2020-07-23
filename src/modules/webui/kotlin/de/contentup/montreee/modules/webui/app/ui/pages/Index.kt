package de.contentup.montreee.modules.webui.app.ui.pages

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.ui.StaticLinks
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.comment
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags.html5Doctype
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags.script
import de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags.use
import de.contentup.montreee.modules.webui.app.ui.util.respondRawHtmlWithSectionComments
import io.ktor.application.*
import io.ktor.util.pipeline.*
import kotlinx.html.*

suspend fun PipelineContext<Unit, ApplicationCall>.index(context: ApplicationContext) {
    call.respondRawHtmlWithSectionComments {

        html5Doctype()

        html {
            lang = "en"

            head {

                title { +"Montreee WebUI" }

                comment("metadata") {
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
                        content = "Montreee WebUI"
                    }
                }

                comment("styles sheets") {
                    styleLink(StaticLinks.CSS.Pace)
                    styleLink(StaticLinks.CSS.Fontawesome)
                    styleLink(StaticLinks.CSS.Montreee)
                }

            }

            body(classes = "montreee-app montreee-dark-theme") {

                comment("sidebar") {
                    div(classes = "montreee-sidebar montreee-sidebar-fixed montreee-sidebar-show") {
                        id = "sidebar"

                        comment("sidebar branding") {
                            div(classes = "montreee-sidebar-brand") {
                                img(classes = "montreee-sidebar-brand-full") {
                                    src = StaticLinks.Assets.MontreeeLogo
                                    width = "118"
                                    height = "46"
                                    alt = "Montreee Logo"
                                }
                            }
                        }

                        comment("sidebar nav") {
                            ul(classes = "montreee-sidebar-nav") {
                                attributes["data-drodpown-accordion"] = "true"

                                li(classes = "montreee-sidebar-nav-item") {

                                    comment("sidebar nav items") {
                                        sidebarNavItem(
                                                "Dashboard",
                                                "main",
                                                "${StaticLinks.Assets.Icons.FreeSymboleDefs}#cui-speedometer"
                                        )
                                        sidebarNavItem(
                                                "Montreee",
                                                "montreee",
                                                "${StaticLinks.Assets.Icons.FreeSymboleDefs}#cui-folder"
                                        )
                                    }

                                }
                            }
                        }

                    }
                }

                comment("content") {
                    div("montreee-wrapper") {

                        comment("header") {
                            div(classes = "montreee-header montreee-header-fixed") {

                                comment("expand sidebar nav button") {
                                    button(classes = "montreee-header-toggler montreee-class-toggler mx-3 py-3 mfs-3") {
                                        id = "sidebar-toggler"
                                        type = ButtonType.button
                                        attributes["data-target"] = "#sidebar"
                                        attributes["data-class"] = "montreee-sidebar-show"
                                        attributes["responsive"] = "true"
                                        span(classes = "montreee-header-toggler-icon") {}
                                    }
                                }

                                comment("search bar") {
                                    div("montreee-searchbar montreee-header-nav") {
                                        div(classes = "input-group") {
                                            div(classes = "input-group-prepend") {
                                                span(classes = "input-group-text") {
                                                    i(classes = "fa fa-search")
                                                }
                                            }
                                            input(classes = "form-control") {
                                                type = InputType.text
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        comment("ui view placeholder") {
                            div {
                                id = "ui-view"
                            }
                        }

                    }
                }

                comment("scripts") {
                    script(src = StaticLinks.JS.Before)
                    script(src = StaticLinks.JS.Lib.JQuery)
                    script(src = StaticLinks.JS.Lib.Pace)
                    script(src = StaticLinks.JS.Lib.CoreUI)
                    script(src = StaticLinks.JS.Lib.IfBreakpoint)
                    script(src = StaticLinks.JS.Sidebar)
                    script(src = StaticLinks.JS.Views)
                }
            }
        }
    }
}

private fun LI.sidebarNavItem(text: String = "", link: String = "", iconLink: String = "") {
    a(classes = "montreee-sidebar-nav-link") {
        href = link
        svg(classes = "montreee-sidebar-nav-icon") {
            use {
                attributes["xlink:href"] = iconLink
            }
        }
        +text
    }
}

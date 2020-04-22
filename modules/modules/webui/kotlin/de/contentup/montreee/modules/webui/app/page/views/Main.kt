package de.contentup.montreee.modules.webui.app.page.views

import de.contentup.montreee.modules.webui.app.StaticLinks
import de.contentup.montreee.modules.webui.app.htmlDsl.comment
import de.contentup.montreee.modules.webui.app.htmlDsl.tags.script
import de.contentup.montreee.modules.webui.app.htmlDsl.tags.styleLink
import de.contentup.montreee.modules.webui.app.util.respondRawHtmlWithSectionComments
import de.contentup.montreee.modules.webui.app.webuiDataFolder
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.div
import kotlinx.html.main

suspend fun PipelineContext<Unit, ApplicationCall>.mainView() {
    call.respondRawHtmlWithSectionComments {
        comment("view") {

            comment("dependencies") {

                comment("stylesheets") {
                    styleLink(StaticLinks.CSS.ChartJS)
                    styleLink(StaticLinks.CSS.CoreUIChartsJS)
                }

                comment("scripts") {
                    script(src = StaticLinks.JS.Lib.ChartJS)
                    script(src = StaticLinks.JS.Lib.CoreUIChartsJS)
                    script(src = StaticLinks.JS.ChartJSResizeFix)
                }

            }

            comment("content") {
                div(classes = "montreee-body fade-in") {
                    main(classes = "montreee-main") {
                        div(classes = "container-fluid") {
                            script(src = "views/main.js")
                            consumer.onTagContentUnsafe {
                                +webuiDataFolder.resolve("views/main.html").readText()
                            }
                        }
                    }
                }
            }
        }
    }
}

package de.contentup.montreee.modules.webui.app.ui

import de.contentup.montreee.modules.webui.app.ApplicationContext
import de.contentup.montreee.modules.webui.app.ui.views.mainView
import de.contentup.montreee.modules.webui.app.ui.views.montreeeView
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.views(context: ApplicationContext) {
    get("main") { mainView(context) }
    get("montreee/{path...}") { montreeeView(context) }
}
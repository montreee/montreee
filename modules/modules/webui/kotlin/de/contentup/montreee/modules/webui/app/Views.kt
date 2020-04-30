package de.contentup.montreee.modules.webui.app

import de.contentup.montreee.modules.webui.app.page.views.mainView
import de.contentup.montreee.modules.webui.app.page.views.montreeeView
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.views(context: ApplicationContext) {
    get("main") { mainView(context) }
    get("montreee/{path...}") { montreeeView(context) }
}
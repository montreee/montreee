package de.contentup.montreee.modules.webui.app

import de.contentup.montreee.Application
import de.contentup.montreee.module.module
import de.contentup.montreee.modules.webui.app.ui.pages.installUIStatusPages
import io.ktor.routing.routing
import java.io.File
import io.ktor.application.Application as KtorApplication

val webuiDataFolder get() = File(Application.module.config.dir.webui)

fun KtorApplication.application(context: ApplicationContext) {
    installUIStatusPages()
    routing { routes(context) }
}

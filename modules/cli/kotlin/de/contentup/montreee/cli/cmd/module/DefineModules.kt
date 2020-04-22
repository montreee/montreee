package de.contentup.montreee.cli.cmd.module

import de.contentup.montreee.modules.api.ApiModule
import de.contentup.montreee.modules.core.CoreModule
import de.contentup.montreee.modules.database.DatabaseModule
import de.contentup.montreee.modules.render.RenderModule
import de.contentup.montreee.modules.standalone.StandaloneModule
import de.contentup.montreee.modules.webui.WebUIModule

fun ModuleLoader.defineModules() {
    module("standalone", "default") { StandaloneModule(it) }
    module("core") { CoreModule(it) }
    module("api") { ApiModule(it) }
    module("render") { RenderModule(it) }
    module("database", "db") { DatabaseModule(it) }
    module("webui", "wui") { WebUIModule(it) }
}

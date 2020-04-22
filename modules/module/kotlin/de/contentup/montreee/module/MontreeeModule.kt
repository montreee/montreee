package de.contentup.montreee.module

import amber.config.impl.YamlConfig
import amber.event.AsyncExecutor
import amber.logging.Logger
import amber.source.Source
import amber.source.provider.LambdaSource
import amber.source.provider.ResourceContentSource
import de.contentup.montreee.Application
import de.contentup.montreee.ApplicationInterface
import de.contentup.montreee.Version
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import montreee.plugin.PluginLoader
import java.io.File

abstract class MontreeeModule(final override val name: String, override val version: Version) :
        ApplicationInterface {

    final override val context by lazy { MontreeeModuleContext(this) }
    final override val logger = Logger("$name Logger", executor = AsyncExecutor(context.log))
    final override val config = runBlocking(context.startup) {
        MontreeeConfig(
                YamlConfig(
                        withContext(context.io) {
                            ResourceContentSource("config.yaml").read()
                        }
                )
        )
    }

    val plugins by lazy {
        runBlocking(context.startup) {
            val sources = withContext(context.io) {
                mutableListOf<Source<ByteArray>>().apply {
                    val pluginDir = File(this@MontreeeModule.config.dir.plugins)
                    if (!(pluginDir.exists() && pluginDir.isDirectory)) {
                        throw Exception("${pluginDir.absolutePath} is not a valid plugin directory") //TODO redo exception
                    }
                    pluginDir.walkTopDown().forEach {
                        if (it.exists() && it.isFile) add(LambdaSource { it.readBytes() })
                    }
                }
            }
            PluginLoader(sources).load()
        }
    }
}

val Application.module: MontreeeModule
    get() = if (delegate is MontreeeModule) delegate as MontreeeModule
    else throw Exception("Running Application isn't a MontreeeModule.") //TODO redo exception

package montreee

import amber.collections.sync
import amber.coroutines.OptimalCoroutineScope
import amber.sync.Synchronized
import montreee.context.Context
import montreee.events.context
import montreee.features.FeatureFactory
import montreee.module.ModuleInfoResolver
import montreee.plugin.Plugin
import montreee.plugin.phases.FeaturesContext
import montreee.render.Renderer

class Engine : Synchronized by Synchronized() {
    private val features = mutableListOf<FeatureFactory>().sync(this)

    suspend fun install(vararg plugin: Plugin) {
        install(plugin.toList())
    }

    suspend fun install(plugin: List<Plugin>) {
        plugin.forEach {
            val context = FeaturesContext()
            it.features.execute(context)
            context.result.value.forEach {
                install(it)
            }
        }
    }

    suspend fun install(vararg feature: FeatureFactory) {
        feature.forEach {
            features.add(it)
        }
    }

    fun createContext(): Context = synchronized {
        val instance = Instance()
        features.forEach {
            instance.install(it)
        }
        instance.context.apply {
            events.context.create.fire(this)
        }
    }

    fun newRenderer() = synchronized {
        Renderer(createContext())
    }

    fun newModuleInfoResolver() = synchronized {
        ModuleInfoResolver(createContext())
    }

    private class Instance {
        val context = Context(OptimalCoroutineScope("Engine"))
        val installedFeatures = mutableListOf<FeatureFactory>()
        fun install(feature: FeatureFactory) {
            feature.invoke().apply {
                init(context)
                install()
            }
            installedFeatures.add(feature)
        }
    }
}

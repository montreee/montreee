package montreee.plugin

import amber.source.Source
import amber.trial.trial
import montreee.plugin.classloder.JarInputStreamClassLoader
import montreee.plugin.phases.SetupContext
import java.io.ByteArrayInputStream
import java.util.jar.JarInputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

class PluginLoader(private val sources: List<Source<ByteArray>>) {

    constructor(vararg source: Source<ByteArray>) : this(source.toList())

    private val classLoader = JarInputStreamClassLoader()

    suspend fun load() = load(sources)

    suspend fun load(
            sources: List<Source<ByteArray>>, loadedClasses: MutableList<KClass<*>> = mutableListOf()
    ): MutableList<Plugin> {
        val plugins = mutableListOf<Plugin>()
        sources.forEach {

            val inputStream = JarInputStream(ByteArrayInputStream(it.read()))
            classLoader.addSource(inputStream)
            classLoader.loadAllClasses().filter {
                trial {
                    it.kotlin.isSubclassOf(Plugin::class)
                } alternate { false } && loadedClasses.none { kclass -> kclass === it.kotlin }
            }.forEach {
                loadedClasses.add(it.kotlin)
                val context = SetupContext()
                plugins.add((it.kotlin.createInstance() as Plugin).apply {
                    load()
                    setup.execute(context)
                })
                load(context.result.value, loadedClasses).forEach { plugins.add(it) }
            }
        }
        return plugins
    }
}

import amber.config.ConfigException
import amber.result.onSuccess
import amber.serialization.normalize
import amber.source.Source
import amber.source.provider.LambdaSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObjectSerializer
import montreee.Engine
import montreee.features.standard.installStandardFeatures
import montreee.plugin.PluginLoader
import montreee.render.Renderer
import java.io.File
import kotlin.system.measureNanoTime

val plugins by lazy {
    val pluginLoader = PluginLoader(mutableListOf<Source<ByteArray>>().apply {
        val pluginDir = File("plugins")
        if (!(pluginDir.exists() && pluginDir.isDirectory)) {
            throw ConfigException("${pluginDir.absolutePath} is not a valid plugin directory")
        }
        pluginDir.walkTopDown().forEach {
            if (it.exists() && it.isFile && it.absolutePath.endsWith(".jar")) add(LambdaSource { it.readBytes() })
        }
    })
    runBlocking {
        pluginLoader.load()
    }
}

val engine by lazy {
    runBlocking {
        Engine().apply {
            installStandardFeatures()
            install(plugins)
        }
    }
}

suspend fun main() {
    var renderer: Renderer? = null
    val init = measureNanoTime {
        renderer = engine.newRenderer()
    } / 1000000F
    println("initialized in ${"%.6f".format(init)} millis")
    delay(500)
    var lastResult = ""
    while (true) {
        val update = measureNanoTime {
            renderer?.createPage(
                    "index.html", kotlinx.serialization.json.Json.parse(
                    JsonObjectSerializer, TestStuff.testPageInput()
            ).normalize()
            )

        } / 1000000F
        val render = measureNanoTime {
            renderer?.invoke()?.get("index.html")?.onSuccess {
                val resultString = value.read()
                assert(resultString.isNotBlank() && (resultString == lastResult || lastResult.isEmpty()))
                lastResult = resultString
                return@measureNanoTime
            }
            assert(false)
        } / 1000000F
        println(
                "rendered in ${"%.6f".format(update + render)} millis (" +
                "update ${"%.6f".format(update)} millis, " +
                "render ${"%.6f".format(render)} millis, " +
                ")"
        )
    }
}

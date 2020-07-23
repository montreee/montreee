package montreee.plugin.phases

import amber.source.Source
import amber.source.provider.LambdaSource

class SetupContext {

    private val pluginSources = mutableListOf<Source<ByteArray>>()

    fun loadPlugins(source: Source<ByteArray>) {
        pluginSources.add(source)
    }

    fun loadPlugins(source: ByteArray) {
        pluginSources.add(LambdaSource { source })
    }

    val result get() = Result(pluginSources)

    data class Result(val value: List<Source<ByteArray>>)
}

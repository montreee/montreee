package amber.config.impl

import amber.config.Config
import amber.trial.trial

open class StackConfig(vararg impl: Config) : Config {

    private val impls = mutableListOf<Config>().apply {
        impl.reversed().forEach { add(it) }
    }

    fun load(config: Config) = impls.add(config)

    override fun find(path: String): String? {
        var result: String? = null
        impls.reversed().first {
            trial {
                result = it.get(path)
                true
            } alternate {
                false
            }
        }
        return result
    }
}

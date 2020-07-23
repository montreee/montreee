package montreee.plugin

import montreee.pipeline.Pipeline
import montreee.plugin.phases.FeaturesContext
import montreee.plugin.phases.SetupContext

open class Plugin {
    val setup = Pipeline<SetupContext>()
    val features = Pipeline<FeaturesContext>()

    //todo decide on removing
    open fun load() {}
}

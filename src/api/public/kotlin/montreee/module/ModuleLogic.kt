package montreee.module

import montreee.module.phases.*
import montreee.pipeline.Pipeline

class ModuleLogic {
    val info = Pipeline<InfoContext>()
    val setup = Pipeline<SetupContext>()
    val feature = Pipeline<FeatureContext>()
    val render = Pipeline<RenderContext>()
    val final = Pipeline<FinalContext>()
}

package montreee.module

abstract class ModulePhaseContext(private val module: ModuleContext) {
    val parameter get() = module.parameter
    val data get() = module.data
    val page get() = module.page
    val context get() = module.context
    val result get() = module.result
}

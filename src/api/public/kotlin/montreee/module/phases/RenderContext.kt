package montreee.module.phases

import montreee.module.ModuleContext
import montreee.module.ModuleOutput
import montreee.module.ModulePhaseContext

class RenderContext(moduleContext: ModuleContext) : ModulePhaseContext(moduleContext) {

    private var _out: ModuleOutput? = null
    fun output(out: ModuleOutput) {
        _out = out
    }

    val out: ModuleOutput get() = _out!!
}

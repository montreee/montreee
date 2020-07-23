package montreee.dsl.module

import montreee.module.phases.RenderContext

@ModuleDsl
inline fun RenderContext.out(block: ModuleOutputScope.() -> Unit) = ModuleOutputScope(this).block()

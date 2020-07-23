package montreee.module

import montreee.context.Context
import montreee.module.phases.InfoContext
import montreee.page.Page
import montreee.parameter.ParameterStore

class ModuleInfoResolver(val context: Context) {
    operator fun invoke() = context.modules.map {
        val context = InfoContext(
                ModuleContext(
                        context, Page(), ParameterStore()
                )
        )
        val moduleLogic = it.value.invoke()
        moduleLogic.info.execute(context)
        return@map Pair(it.key, ModuleInfo(context))
    }.toMap()
}
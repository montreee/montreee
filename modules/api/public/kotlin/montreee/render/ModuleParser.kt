package montreee.render

import montreee.context.Context
import montreee.module.Module
import montreee.page.Page
import montreee.parameter.standard.ModuleParameter

class ModuleParser(private val context: Context, private val page: Page, private val input: Map<String, Any?>) {
    operator fun invoke(): Module {
        val moduleParameter = ModuleParameter()
        moduleParameter.init(context, page, input)

        return moduleParameter.load()
    }
}

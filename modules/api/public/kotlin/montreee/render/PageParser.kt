package montreee.render

import amber.result.Result
import montreee.ParsingException
import montreee.context.Context
import montreee.module.Module
import montreee.page.Page

internal class PageParser(
        private val context: Context, private val page: Page, private val input: Map<String, Any?>
) {

    operator fun invoke(): Result<Module, Throwable> {
        return try {
            val moduleParser = ModuleParser(context, page, input)
            Result.Success(moduleParser())
        } catch (e: Exception) {
            Result.Failure(ParsingException("Parsing failed.", e))
        }
    }
}

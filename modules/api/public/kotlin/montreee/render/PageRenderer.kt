package montreee.render

import amber.result.Result
import amber.result.onFailure
import amber.result.onSuccess
import montreee.context.Context
import montreee.page.Page
import montreee.result.ResultElement

internal class PageRenderer constructor(val context: Context, val page: Page, val input: Map<String, Any?>) {
    operator fun invoke(): Result<ResultElement, Throwable> {
        val parser = PageParser(context, page, input)
        val parsingResult = parser()
        var result: Result<ResultElement, Throwable>? = null
        parsingResult.onSuccess {
            result = try {
                val mainModuleRenderer = ModuleRenderer(context, page, value)
                val mainModuleResult = mainModuleRenderer()
                Result.Success(mainModuleResult)
            } catch (e: Exception) {
                Result.Failure(e)
            }
        }
        parsingResult.onFailure {
            result = Result.Failure(reports)
        }
        return result!!
    }
}

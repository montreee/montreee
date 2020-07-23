package montreee.parameter

import montreee.context.Context
import montreee.page.Page

class ParameterLoader<T>(private val parser: Parser<Any?, T>) {

    private var input: Any? = null

    fun load() = parser.parse(input)
    operator fun invoke() = load()

    fun init(context: Context, page: Page, input: Any?) {
        parser.setup(context, page)
        this.input = input
    }
}

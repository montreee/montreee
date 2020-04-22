package montreee.parameter

import amber.trial.trial
import montreee.ParsingException
import montreee.context.Context
import montreee.page.Page

abstract class Parser<I, T>() {

    private fun errorCallInitBefore(): Nothing =
            throw ParsingException("you have to call setup function before using this")

    private lateinit var _context: Context
    val context get() = trial { _context } alternate { errorCallInitBefore() }

    private lateinit var _page: Page
    val page get() = trial { _page } alternate { errorCallInitBefore() }

    constructor(context: Context, page: Page) : this() {
        setup(context, page)
    }

    fun setup(context: Context, page: Page): Parser<I, T> {
        _context = context
        _page = page
        return this
    }

    abstract fun parse(input: I): T
}

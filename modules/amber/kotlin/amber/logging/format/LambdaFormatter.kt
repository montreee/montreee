package amber.logging.format

import amber.logging.Log

fun formatter(block: Log.() -> String) = LambdaFormatter(block)

class LambdaFormatter(private val block: Log.() -> String) : Formatter {
    override fun Log.format() = block()
}
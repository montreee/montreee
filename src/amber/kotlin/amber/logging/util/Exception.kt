package amber.logging.util

import java.io.PrintWriter
import java.io.StringWriter

fun Throwable.stackTraceAsSting(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    printStackTrace(pw)
    return sw.toString()
}


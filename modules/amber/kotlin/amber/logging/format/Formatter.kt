package amber.logging.format

import amber.logging.Log

interface Formatter {
    operator fun invoke(log: Log): String = log.format()
    fun Log.format(): String
}

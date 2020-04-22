package amber.logging.receiver

import amber.logging.Log
import amber.logging.LogLevel
import amber.logging.LogReceiver
import amber.logging.format.Formats
import amber.logging.format.Formatter

class SimpleConsoleLogReceiver(val level: LogLevel = LogLevel.TRACE, val formatter: Formatter = Formats.Default) :
        LogReceiver() {

    override fun onLog(log: Log) {
        if (!log.level.isGreaterOrEqual(level)) return

        println(formatter(log).removeSuffix("\n"))
    }
}

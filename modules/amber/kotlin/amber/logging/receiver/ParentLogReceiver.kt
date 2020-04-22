package amber.logging.receiver

import amber.logging.LazyLog
import amber.logging.Log
import amber.logging.LogReceiver
import amber.logging.Logger
import com.soywiz.klock.DateTime

class ParentLogReceiver(private val parent: Logger, val reuseTime: Boolean = false) : LogReceiver() {
    override fun onLog(log: Log) {
        val time = if (reuseTime) log.time else DateTime.now()
        parent.log(LazyLog(parent, log.level, time) {
            +log
        })
    }
}

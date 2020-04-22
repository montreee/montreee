package amber.logging

import amber.event.Listener

abstract class LogReceiver : Listener<Log> {
    override fun Log.on() = onLog(this)
    abstract fun onLog(log: Log)
}

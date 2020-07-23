package amber.sevices

import amber.properties.Property
import amber.properties.event.onChange

abstract class Service {
    val isRunningProperty = Property<Boolean>(false)
    var isRunning by isRunningProperty

    init {
        isRunningProperty.onChange {
            if (it) {
                onStart()
            } else {
                onStop()
            }
        }
    }

    fun start() {
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }

    abstract fun onStart()

    abstract fun onStop()
}

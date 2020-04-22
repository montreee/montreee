package amber.sync.exec

import kotlinx.coroutines.channels.Channel

abstract class SynchronizedExecutable<T>(private val waitChannel: Channel<Boolean> = Channel(1)) {

    private var _result: T? = null
    val result: T
        get() = _result ?: throw Exception("Result not available.")

    suspend fun execute() {
        _result = run()
        waitChannel.send(true)
    }

    suspend fun join() {
        waitChannel.receive()
        waitChannel.close()
    }

    abstract fun run(): T
}
package amber.sync.exec

import kotlinx.coroutines.channels.Channel

class LambdaSynchronizedExecutable<T>(private val block: () -> T, waitChannel: Channel<Boolean> = Channel(1)) :
        SynchronizedExecutable<T>(waitChannel) {

    override fun run() = block()
}

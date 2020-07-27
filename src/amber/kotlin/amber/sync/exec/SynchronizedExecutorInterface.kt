package amber.sync.exec

interface SynchronizedExecutorInterface {
    fun contains(thread: Thread): Boolean
    suspend fun stop()
    suspend fun execute(synchronizedExecutable: SynchronizedExecutable<*>)
}
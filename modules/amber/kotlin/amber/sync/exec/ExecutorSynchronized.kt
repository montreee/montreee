package amber.sync.exec

import amber.sync.Synchronized
import kotlinx.coroutines.runBlocking

interface ExecutorSynchronized : Synchronized, AutoCloseable {

    val executor: SynchronizedExecutorInterface

    override fun <T> synchronized(block: () -> T): T {
        if (executor.contains(Thread.currentThread())) return block()

        val executable = LambdaSynchronizedExecutable(block)
        runBlocking {
            executor.execute(executable)
            executable.join()
        }
        return executable.result
    }

    override suspend fun <T> asyncSynchronized(block: () -> T): T {
        if (executor.contains(Thread.currentThread())) return block()

        val executable = LambdaSynchronizedExecutable<T>(block)
        executor.execute(executable)
        executable.join()
        return executable.result
    }

    override fun close() {
        runBlocking {
            executor.stop()
        }
    }
}

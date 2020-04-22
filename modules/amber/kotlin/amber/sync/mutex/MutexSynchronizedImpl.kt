package amber.sync.mutex

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MutexSynchronizedImpl(private val mutex: Mutex = Mutex()) : MutexSynchronized {

    init {
        TODO("Is not working properly! Unsafe use. Use Executor instead!")
    }

    override fun <T> synchronized(block: () -> T): T {
        return if (mutex.holdsLock(Thread.currentThread())) block()
        else {
            var result: T? = null
            runBlocking {
                mutex.withLock(Thread.currentThread()) {
                    result = block()
                }
            }
            result!!
        }
    }

    override suspend fun <T> asyncSynchronized(block: () -> T): T {
        return if (mutex.holdsLock(Thread.currentThread())) block()
        else {
            var result: T? = null
            mutex.withLock(Thread.currentThread()) {
                result = block()
            }
            result!!
        }
    }
}

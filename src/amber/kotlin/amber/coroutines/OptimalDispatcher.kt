package amber.coroutines

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

class OptimalDispatcher(workerThreadNamePrefix: String) : CoroutineDispatcher() {

    private val workerId = atomic(0)

    private val impl = Executors.newFixedThreadPool(parallelism, ThreadFactory {
        Thread(it, "$workerThreadNamePrefix (${workerId.getAndIncrement()})")
    }).asCoroutineDispatcher()

    override val key: CoroutineContext.Key<*>
        get() = impl.key

    override fun dispatch(context: CoroutineContext, block: Runnable) = impl.dispatch(context, block)

    @InternalCoroutinesApi
    override fun dispatchYield(context: CoroutineContext, block: Runnable) {
        impl.dispatchYield(context, block)
    }

    override fun equals(other: Any?): Boolean {
        return impl.equals(other)
    }

    override fun <R> fold(initial: R, operation: (R, CoroutineContext.Element) -> R): R {
        return impl.fold(initial, operation)
    }

    override fun hashCode(): Int {
        return impl.hashCode()
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return impl.isDispatchNeeded(context)
    }

    override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
        return impl.minusKey(key)
    }

    override fun plus(context: CoroutineContext): CoroutineContext {
        return impl.plus(context)
    }

    @InternalCoroutinesApi
    override fun releaseInterceptedContinuation(continuation: Continuation<*>) {
        impl.releaseInterceptedContinuation(continuation)
    }

    override fun toString(): String {
        return impl.toString()
    }
}

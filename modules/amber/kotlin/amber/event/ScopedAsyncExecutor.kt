package amber.event

import amber.coroutines.scope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class AsyncExecutor<T>(private val context: CoroutineContext) : Executor<T>() {
    override fun execute(data: T, listeners: List<Listener<T>>) {
        context.scope().launch {
            listeners.forEach {
                it(data)
            }
        }
    }
}

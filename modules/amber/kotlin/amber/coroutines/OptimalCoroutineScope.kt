package amber.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class OptimalCoroutineScope(workerThreadNamePrefix: String) : CoroutineScope {
    override val coroutineContext: CoroutineContext = OptimalDispatcher(workerThreadNamePrefix)
}

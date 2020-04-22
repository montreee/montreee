package amber.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

open class SingleThreadCoroutineScope(treadName: String) : CoroutineScope {
    override val coroutineContext: CoroutineContext = newSingleThreadContext(treadName)
}

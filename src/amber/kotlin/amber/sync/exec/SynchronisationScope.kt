package amber.sync.exec

import amber.text.randomAlphanumericString
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class SynchronisationScope : CoroutineScope {
    val scopeIdentifier = randomAlphanumericString(128)
    private val impl = SyncThreadCoroutineScope(scopeIdentifier)
    override val coroutineContext: CoroutineContext
        get() = impl.coroutineContext
}

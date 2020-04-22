package amber.sync.exec

import amber.coroutines.SingleThreadCoroutineScope
import amber.text.randomAlphanumericString

class SyncThreadCoroutineScope(private val scopeIdentifier: String = randomAlphanumericString(128)) :
        SingleThreadCoroutineScope("Synchronization Thread $scopeIdentifier")

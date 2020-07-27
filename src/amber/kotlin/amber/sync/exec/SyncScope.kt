package amber.sync.exec

import amber.collections.randomItem
import amber.coroutines.parallelism
import amber.sync.SyncMode
import amber.sync.Synchronized

private val synchronizedImpl = Synchronized(
        syncMode = SyncMode.UNSAFE_EXECUTOR,
        syncExecutor = SynchronizedExecutor(SynchronisationScope())
)

//Todo map Every synchronized to an SyncScope(intelligent Synchronize executors)
private val syncScopes by lazy { mutableListOf<SynchronisationScope>() }

val SyncScope
    get() = synchronizedImpl.synchronized {
        if (syncScopes.size < parallelism) {
            val newScope = SynchronisationScope()
            syncScopes.add(newScope)
            newScope
        } else syncScopes.randomItem()
    }

package amber.sync.safeexec

import amber.sync.exec.ExecutorSynchronized
import amber.sync.exec.SynchronizedExecutorInterface

class SafeExecutorSynchronizedImpl(
        override val executor: SynchronizedExecutorInterface = SafeSyncManager.createExecutor()
) : ExecutorSynchronized
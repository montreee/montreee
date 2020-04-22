package amber.sync.exec

class ExecutorSynchronizedImpl(override val executor: SynchronizedExecutor = SynchronizedExecutor(SyncScope)) :
        ExecutorSynchronized

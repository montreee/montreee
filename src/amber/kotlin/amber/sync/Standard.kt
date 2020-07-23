package amber.sync

import amber.sync.exec.ExecutorSynchronizedImpl
import amber.sync.exec.SynchronisationScope
import amber.sync.exec.SynchronizedExecutor
import amber.sync.kotlin.KotlinSynchronizedImpl
import amber.sync.mutex.MutexSynchronizedImpl
import amber.sync.safeexec.SafeExecutorSynchronizedImpl
import kotlinx.coroutines.sync.Mutex

@Suppress("FunctionName")
fun Synchronized(
        syncMode: SyncMode = SyncMode.KOTLIN, mutex: Mutex? = null, syncExecutor: SynchronizedExecutor? = null
) = when (syncMode) {
    SyncMode.KOTLIN          -> KotlinSynchronizedImpl()
    SyncMode.SAFE_EXECUTOR   -> SafeExecutorSynchronizedImpl()
    SyncMode.HEAVY_EXECUTOR  -> ExecutorSynchronizedImpl(SynchronizedExecutor(SynchronisationScope()))
    SyncMode.UNSAFE_EXECUTOR -> syncExecutor?.let { ExecutorSynchronizedImpl(it) } ?: ExecutorSynchronizedImpl()
    SyncMode.MUTEX           -> mutex?.let { MutexSynchronizedImpl(it) } ?: MutexSynchronizedImpl()
}

enum class SyncMode {
    KOTLIN, SAFE_EXECUTOR, HEAVY_EXECUTOR, UNSAFE_EXECUTOR, MUTEX
}

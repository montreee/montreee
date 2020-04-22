package amber.sync

import amber.sync.exec.ExecutorSynchronizedImpl
import amber.sync.exec.SynchronisationScope
import amber.sync.exec.SynchronizedExecutor
import amber.sync.kotlin.KotlinSynchronizedImpl
import amber.sync.mutex.MutexSynchronizedImpl
import amber.sync.safeexec.SafeExecutorSynchronizedImpl
import kotlinx.coroutines.sync.Mutex

interface Synchronized {
    fun <T> synchronized(block: () -> T): T
    suspend fun <T> asyncSynchronized(block: () -> T): T
}

@Suppress("FunctionName")
fun Synchronized(
        syncMode: SyncMode = SyncMode.KOTLIN, mutex: Mutex? = null, syncExecutor: SynchronizedExecutor? = null
) = when (syncMode) {
    SyncMode.SAFE_EXECUTOR   -> SafeExecutorSynchronizedImpl()
    SyncMode.HEAVY_EXECUTOR  -> ExecutorSynchronizedImpl(SynchronizedExecutor(SynchronisationScope()))
    SyncMode.UNSAFE_EXECUTOR -> if (syncExecutor == null) ExecutorSynchronizedImpl() else ExecutorSynchronizedImpl(
            syncExecutor
    )
    SyncMode.MUTEX           -> if (mutex == null) MutexSynchronizedImpl() else MutexSynchronizedImpl(mutex)
    SyncMode.KOTLIN          -> KotlinSynchronizedImpl()
}

enum class SyncMode {
    HEAVY_EXECUTOR, SAFE_EXECUTOR, UNSAFE_EXECUTOR, MUTEX, KOTLIN
}

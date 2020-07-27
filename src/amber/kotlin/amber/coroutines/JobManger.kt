package amber.coroutines

import amber.collections.SyncMutableList
import amber.collections.forEachSave
import amber.collections.syncMutableListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class JobManger(private val jobs: SyncMutableList<Job> = syncMutableListOf()) {

    fun add(job: Job) {
        jobs.add(job)
    }

    suspend fun joinAll() {
        jobs.forEachSave { it.join() }
    }

    fun cancelAll() {
        jobs.forEachSave { it.cancel() }
    }

    suspend fun cancelAndJoinAll() {
        jobs.forEachSave { it.cancel() }
        jobs.forEachSave { it.join() }
    }

    fun CoroutineScope.launch(
            start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit
    ): Job {
        val job = launch(this, coroutineContext, start, block)
        add(job)
        return job
    }
}

class CoroutineScopeJobManager(coroutineScope: CoroutineScope) : JobManger(), CoroutineScope by coroutineScope

private fun launch(
        thisRef: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
) = thisRef.launch(context, start, block)

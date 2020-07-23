package amber.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class JoinAllJobsScope(val coroutineScope: CoroutineScope, val jobManger: JobManger) {
    fun launch(block: suspend CoroutineScope.() -> Unit) {
        jobManger.add(coroutineScope.launch(coroutineScope.coroutineContext, CoroutineStart.DEFAULT, block))
    }
}

fun CoroutineScope.joinAllJobsNonSuspending(block: CoroutineScopeJobManager.() -> Unit) {
    val jobManger = CoroutineScopeJobManager(this)
    jobManger.block()
    runBlocking { jobManger.joinAll() }
}

suspend fun CoroutineScope.joinAllJobs(block: suspend CoroutineScopeJobManager.() -> Unit) {
    val jobManger = CoroutineScopeJobManager(this)
    jobManger.block()
    jobManger.joinAll()
}

fun CoroutineContext.scope() = CoroutineScope(this)
inline fun <T> CoroutineContext.scope(block: CoroutineScope.() -> T) = this.scope().block()
suspend operator fun <T> CoroutineContext.invoke(block: suspend CoroutineScope.() -> T) =
        withContext(this, block)
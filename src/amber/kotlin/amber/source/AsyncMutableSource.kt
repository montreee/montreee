package amber.source

import kotlinx.coroutines.runBlocking

interface AsyncMutableSource<T> : AsyncSource<T>, MutableSource<T> {

    override fun write(t: T) = runBlocking { writeAsync(t) }

    suspend fun writeAsync(t: T)
}

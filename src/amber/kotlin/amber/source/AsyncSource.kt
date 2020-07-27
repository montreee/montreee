package amber.source

import kotlinx.coroutines.runBlocking

interface AsyncSource<T> : Source<T> {

    override fun read() = runBlocking { readAsync() }

    suspend fun readAsync(): T
}

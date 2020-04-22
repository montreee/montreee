package amber.source.provider

import amber.source.AsyncMutableSource

class AsyncMutableLambdaSource<T>(
        private val readBlock: suspend () -> T, private val writeBlock: suspend T.() -> Unit
) : AsyncMutableSource<T> {

    override suspend fun writeAsync(t: T) = t.writeBlock()

    override suspend fun readAsync() = readBlock()
}

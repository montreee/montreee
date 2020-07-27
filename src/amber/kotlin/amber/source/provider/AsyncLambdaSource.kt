package amber.source.provider

import amber.source.AsyncSource

class AsyncLambdaSource<T>(private val readBlock: suspend () -> T) : AsyncSource<T> {
    override suspend fun readAsync() = readBlock()
}

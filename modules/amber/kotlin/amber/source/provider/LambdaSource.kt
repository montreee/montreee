package amber.source.provider

import amber.source.Source

class LambdaSource<T>(private val readBlock: () -> T) : Source<T> {
    override fun read() = readBlock()
}

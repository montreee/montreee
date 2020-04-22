package amber.source.provider

import amber.source.MutableSource

class MutableLambdaSource<T>(private val readBlock: () -> T, private val writeBlock: T.() -> Unit) : MutableSource<T> {
    override fun write(t: T) = t.writeBlock()

    override fun read() = readBlock()
}

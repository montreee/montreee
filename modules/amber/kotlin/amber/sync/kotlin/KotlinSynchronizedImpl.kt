package amber.sync.kotlin

class KotlinSynchronizedImpl : KotlinSynchronized {

    override fun <T> synchronized(block: () -> T): T {
        val ret: T
        kotlin.synchronized(this) {
            ret = block()
        }
        return ret
    }

    override suspend fun <T> asyncSynchronized(block: () -> T): T {
        val ret: T
        kotlin.synchronized(this) {
            ret = block()
        }
        return ret
    }
}

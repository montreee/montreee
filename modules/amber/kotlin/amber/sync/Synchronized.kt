package amber.sync

interface Synchronized {
    fun <T> synchronized(block: () -> T): T
    suspend fun <T> asyncSynchronized(block: () -> T): T
}

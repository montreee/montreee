package amber.trial

class AsyncTrial<T>(private val block: suspend () -> T) {
    private var error: Throwable? = null
    private var result: T? = null

    internal suspend fun execute() {
        try {
            result = block()
        } catch (t: Throwable) {
            error = t
        }
    }

    suspend infix fun alternate(block: suspend (Throwable) -> T): T {
        return if (error != null) block(error!!) else result ?: block(NullPointerException())
    }

    suspend infix fun catch(block: suspend (Throwable) -> Unit): AsyncTrial<T> {
        if (error != null) block(error!!)
        return this
    }
}

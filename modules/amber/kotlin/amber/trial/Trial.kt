package amber.trial

class Trial<T>(private val block: () -> T) {
    private var error: Throwable? = null
    private var result: T? = null

    internal fun execute() {
        try {
            result = block()
        } catch (t: Throwable) {
            error = t
        }
    }

    infix fun alternate(block: (Throwable) -> T): T {
        return if (error != null) block(error!!) else result!!
    }

    infix fun catch(block: (Throwable) -> Unit): Trial<T> {
        if (error != null) block(error!!)
        return this
    }
}

package amber.pipeline

interface Phase<C> {
    suspend fun run(context: C)
}

package amber.pipeline

abstract class PipelineBase<C>(
        phases: MutableList<Phase<C>>? = null, executor: Executor<C>? = null
) : Phase<C> {

    protected val _phases = phases ?: mutableListOf()
    val phases get() = _phases.toList()

    protected val executor: Executor<C> = executor ?: Executor()

    suspend fun execute(context: C): C {
        run(context)
        return context
    }
}

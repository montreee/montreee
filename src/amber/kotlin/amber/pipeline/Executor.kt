package amber.pipeline

open class Executor<C> {
    open suspend fun execute(phases: List<Phase<C>>, context: C) {
        phases.forEach {
            it.run(context)
        }
    }
}

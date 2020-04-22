package montreee.pipeline

open class Executor<C> {
    open fun execute(phases: List<Phase<C>>, context: C) {
        phases.forEach {
            it.run(context)
        }
    }
}

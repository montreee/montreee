package montreee.pipeline

open class ImmutablePipeline<C>(phases: MutableList<Phase<C>>? = null, executor: Executor<C>? = null) :
        PipelineBase<C>(phases, executor) {

    override fun run(context: C) {
        executor.execute(phases, context)
    }
}

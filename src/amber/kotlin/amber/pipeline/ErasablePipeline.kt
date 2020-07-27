package amber.pipeline

class ErasablePipeline<C>(phases: MutableList<Phase<C>>? = null, executor: Executor<C>? = null) : Pipeline<C>(
        phases, executor
) {

    fun remove(phase: Phase<C>) {
        _phases.remove(phase)
    }

    fun clear() {
        _phases.clear()
    }
}

package montreee.pipeline

open class Pipeline<C>(phases: MutableList<Phase<C>>? = null, executor: Executor<C>? = null) :
        ImmutablePipeline<C>(phases, executor) {

    operator fun Phase<C>.unaryPlus() {
        add(this@unaryPlus)
    }

    fun add(phase: Phase<C>) {
        _phases.add(phase)
    }

    fun push(phase: Phase<C>) {
        val _phases = phases
        if (_phases.isEmpty()) {
            add(phase)
            return
        }
        addBefore(_phases[0], phase)
    }

    fun addBefore(before: Phase<C>, phase: Phase<C>) {
        val index = _phases.indexOf(before)
        if (index >= 0) _phases.add(_phases.indexOf(before), phase)
    }

    fun addAfter(after: Phase<C>, phase: Phase<C>) {
        val index = _phases.indexOf(after)
        if (index >= 0) _phases.add(index + 1, phase)
    }

    fun final(): ImmutablePipeline<C> {
        return ImmutablePipeline<C>(_phases, executor)
    }
}

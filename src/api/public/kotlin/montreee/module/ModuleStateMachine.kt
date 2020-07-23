package montreee.module

import montreee.RenderException
import montreee.module.phases.*
import montreee.pipeline.Pipeline

internal class ModuleStateMachine(
        private val infoPipeline: Pipeline<InfoContext>,
        private val setupPipeline: Pipeline<SetupContext>,
        private val featurePipeline: Pipeline<FeatureContext>,
        private val renderPipeline: Pipeline<RenderContext>,
        private val finalPipeline: Pipeline<FinalContext>
) : Iterator<ModuleState> {

    var current = ModuleState.NONE
        private set

    private val states = listOf(
            ModuleState.BEFORE,
            ModuleState.INFO,
            ModuleState.SETUP,
            ModuleState.FEATURE,
            ModuleState.RENDER,
            ModuleState.FINAL,
            ModuleState.AFTER
    )
    private val iterator = states.iterator()

    @Suppress("UNCHECKED_CAST")
    fun <T> currentPipeline() = pipeline(current) as T

    fun currentPipeline() = pipeline(current)

    fun pipeline(of: ModuleState): Pipeline<*> {
        return when (of) {
            ModuleState.INFO    -> infoPipeline
            ModuleState.SETUP   -> setupPipeline
            ModuleState.FEATURE -> featurePipeline
            ModuleState.RENDER  -> renderPipeline
            ModuleState.FINAL   -> finalPipeline
            else                -> throw RenderException("No pipeline defined for \"${of.name}\".")
        }
    }

    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): ModuleState {
        val next = iterator.next()
        current = next
        return next
    }

}

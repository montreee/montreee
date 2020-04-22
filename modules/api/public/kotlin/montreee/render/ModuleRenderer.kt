package montreee.render

import montreee.context.Context
import montreee.events.module
import montreee.module.*
import montreee.module.phases.*
import montreee.page.Page
import montreee.parameter.ParameterStore
import montreee.pipeline.Pipeline
import montreee.result.ResultElement

internal class ModuleRenderer internal constructor(val context: Context, val page: Page, val module: Module) {

    private val resultList = mutableListOf<ResultElement>()

    private val moduleContext = ModuleContext(context, page, ParameterStore(module.parameter), out = resultList)

    operator fun invoke(): ResultElement {
        val moduleStateMachine = ModuleStateMachine(
                module.logic.info, module.logic.setup, module.logic.feature, module.logic.render, module.logic.final
        )
        moduleStateMachine.forEach {
            when (it) {
                ModuleState.BEFORE  -> {
                    context.events.module.before.fire(module.logic)
                }
                ModuleState.INFO    -> {
                    moduleStateMachine.currentPipeline<Pipeline<InfoContext>>().apply {
                        execute(InfoContext(moduleContext).apply { context.events.module.info.fire(this) })
                    }
                }
                ModuleState.SETUP   -> {
                    moduleStateMachine.currentPipeline<Pipeline<SetupContext>>().apply {
                        execute(SetupContext(moduleContext).apply { context.events.module.setup.fire(this) })
                    }
                }
                ModuleState.FEATURE -> {
                    moduleStateMachine.currentPipeline<Pipeline<FeatureContext>>().apply {
                        execute(FeatureContext(moduleContext).apply { context.events.module.feature.fire(this) })
                    }
                }
                ModuleState.RENDER  -> {
                    moduleStateMachine.currentPipeline<Pipeline<RenderContext>>().apply {
                        execute(RenderContext(moduleContext).apply {
                            output(ModuleOutput(resultList))
                        }.apply { context.events.module.render.fire(this) })
                    }
                }
                ModuleState.FINAL   -> {
                    moduleStateMachine.currentPipeline<Pipeline<FinalContext>>().apply {
                        execute(FinalContext(moduleContext).apply { context.events.module.final.fire(this) })
                    }
                }
                ModuleState.AFTER   -> {
                    context.events.module.after.fire(module.logic)
                }
                else                -> {
                }
            }
        }
        return moduleContext.result
    }
}

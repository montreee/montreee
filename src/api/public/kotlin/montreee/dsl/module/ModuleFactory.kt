package montreee.dsl.module

import montreee.module.ModuleFactory
import montreee.module.ModuleLogic

@ModuleDsl
class LambdaModuleFactory(name: String, val block: () -> ModuleLogic) : ModuleFactory(name) {

    override fun invoke() = block()
}

@ModuleDsl
fun module(name: String, block: ModuleLogic.() -> Unit) = moduleFactory(name) { module(block) }

@ModuleDsl
fun moduleFactory(name: String, block: () -> ModuleLogic) = LambdaModuleFactory(name, block)

@ModuleDsl
fun module(block: ModuleLogic.() -> Unit): ModuleLogic {
    val module = ModuleLogic()
    module.apply(block)
    return module
}
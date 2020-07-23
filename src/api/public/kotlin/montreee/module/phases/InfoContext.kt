package montreee.module.phases

import montreee.descriptor.InputType
import montreee.descriptor.ModuleDescriptor
import montreee.descriptor.ParameterDescriptor
import montreee.module.ModuleContext
import montreee.module.ModulePhaseContext

class InfoContext(moduleContext: ModuleContext) : ModulePhaseContext(moduleContext) {

    val moduleDescriptor: ModuleDescriptor get() = ModuleDescriptor(*requiredParameter.toTypedArray())

    private val requiredParameter = mutableListOf<ParameterDescriptor>()

    fun require(descriptor: ParameterDescriptor) {
        requiredParameter.add(descriptor)
    }

    fun require(parameterName: String, input: InputType, isOptional: Boolean = false) {
        require(ParameterDescriptor(parameterName, input, isOptional))
    }
}

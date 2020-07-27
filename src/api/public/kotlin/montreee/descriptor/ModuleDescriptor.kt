package montreee.descriptor

data class ModuleDescriptor(val parameter: List<ParameterDescriptor>) {
    constructor(vararg parameter: ParameterDescriptor) : this(parameter.toList())
}



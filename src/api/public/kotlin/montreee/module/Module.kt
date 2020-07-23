package montreee.module

import montreee.parameter.Parameter

data class Module(val logic: ModuleLogic, val parameter: Map<String, Parameter<*>>)

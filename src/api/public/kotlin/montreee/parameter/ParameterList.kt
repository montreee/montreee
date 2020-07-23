package montreee.parameter

class ParameterList(private val impl: List<Parameter<*>>) : List<Parameter<*>> by impl

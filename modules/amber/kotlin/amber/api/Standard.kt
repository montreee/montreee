package amber.api

fun constructParameter(block: ParameterList.() -> Unit): ParameterList {
    val parameterList = ParameterList()
    parameterList.apply(block)
    return parameterList
}

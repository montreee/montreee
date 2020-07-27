package montreee.dsl.parameter

import montreee.parameter.Parameter
import montreee.parameter.ParameterFactory

class LambdaParameterFactory<T>(name: String, val block: () -> Parameter<T>) : ParameterFactory<T>(name) {
    override fun invoke() = block()
}

fun <T> parameterFactory(name: String, block: () -> Parameter<T>) = LambdaParameterFactory(name, block)
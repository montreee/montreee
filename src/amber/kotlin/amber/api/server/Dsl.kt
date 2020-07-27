package amber.api.server

import amber.api.MethodPath
import amber.logging.Logger

@ApiDslMaker
fun apiConfiguration(block: Api.Configuration.() -> Unit) = Api.Configuration().apply(block)

@ApiDslMaker
fun Api.Configuration.buildApi(logger: Logger? = null, block: Api.() -> Unit) = api(this, logger, block = block)

@ApiDslMaker
fun api(configuration: Api.Configuration? = null, logger: Logger? = null, block: Api.() -> Unit): Api {
    return when {
        (configuration != null && logger != null) -> Api(configuration, logger)
        (configuration != null && logger == null) -> Api(configuration = configuration)
        (configuration == null && logger != null) -> Api(logger = logger)
        else                                      -> Api()
    }.apply(block)
}

open class LambdaMethod(private val block: suspend Method.MethodScope.() -> Unit) : Method() {
    override suspend fun call(methodScope: MethodScope) {
        methodScope.block()
    }
}

@ApiDslMaker
fun Api.method(methodPath: MethodPath, method: suspend Method.MethodScope.() -> Unit) {
    method(methodPath, LambdaMethod(method))
}

@ApiDslMaker
fun MethodGroup.method(methodPath: MethodPath, method: suspend Method.MethodScope.() -> Unit) {
    method(methodPath, LambdaMethod(method))
}

@ApiDslMaker
fun Api.group(prefix: MethodPath, method: MethodGroup.() -> Unit) {
    MethodGroup(this, prefix).apply(method)
}

class MethodGroup(val api: Api, val prefix: String) {
    fun method(methodPath: MethodPath, method: Method) =
            api.method(if (prefix.isNotBlank()) "$prefix/$methodPath" else methodPath, method)

    fun group(prefix: MethodPath, method: MethodGroup.() -> Unit) {
        MethodGroup(api, if (this.prefix.isNotBlank()) "${this.prefix}/$prefix" else prefix).apply(method)
    }
}

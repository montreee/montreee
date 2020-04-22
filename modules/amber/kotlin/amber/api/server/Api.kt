package amber.api.server

import amber.api.MethodPath
import amber.api.response.Response
import amber.api.server.pipeline.CallContext
import amber.api.server.pipeline.CallInput
import amber.api.server.pipeline.CallPipeline
import amber.logging.Logger
import amber.logging.debug
import amber.pipeline.intercept

class Api(configuration: Configuration = Configuration(), val logger: Logger = Logger("Api Logger")) {

    class Configuration

    private val methodMap = MethodMap()
    private val methodManager = MethodManager(methodMap)

    private val callPipeline = CallPipeline()

    init {
        callPipeline.monitoring.intercept {
            logger.debug {
                +"Api call on ${input.methodPath} with ${input.call.parameter}"
            }
        }
        callPipeline.call.intercept {
            val method = methodManager(input.methodPath)
            method?.call(Method.MethodScope(this@Api, method, input.call))
        }
        callPipeline.fallback.intercept {
            val method = methodManager(input.methodPath)
            if (method == null || method.fallbackResponse) {
                input.call.respond(Response(ByteArray(0)))
            }
        }
    }

    fun method(methodPath: MethodPath, method: Method) {
        methodManager.create(methodPath, method)
    }

    suspend fun call(methodPath: MethodPath, call: Call) {
        callPipeline.execute(CallContext(CallInput(methodPath, call)))
    }
}

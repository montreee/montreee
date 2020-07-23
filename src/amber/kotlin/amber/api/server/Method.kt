package amber.api.server

import amber.api.response.Response

abstract class Method : MethodMap() {

    internal var fallbackResponse: Boolean = true
        private set

    abstract suspend fun call(methodScope: MethodScope)

    class MethodScope(private val api: Api, private val method: Method, private val call: Call) : Call by call {
        fun preventFallback() {
            method.fallbackResponse = false
        }

        fun respond() = respond(Response(ByteArray(0)))

        override fun respond(response: Response) {
            preventFallback()
            call.respond(response)
        }
    }
}

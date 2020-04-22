package amber.api.server

import amber.api.MethodPath

interface ApiImpl {
    val api: Api
    suspend fun call(methodPath: MethodPath, call: Call) = api.call(methodPath, call)
}

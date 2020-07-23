package amber.api.client

import amber.api.MethodPath
import amber.api.ParameterList
import amber.api.response.Response

interface ApiClient {
    fun connect()
    suspend fun call(methodPath: MethodPath, parameter: ParameterList = ParameterList()): Response
    fun disconnect()
}

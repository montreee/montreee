package amber.api.server.impl.local

import amber.api.MethodPath
import amber.api.ParameterList
import amber.api.response.Response
import amber.api.server.Api
import amber.api.server.ApiImpl
import amber.api.server.Session
import amber.api.server.constructCall
import amber.coroutines.SingleThreadCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocalApiImplementation(override val api: Api) : ApiImpl {

    private val coroutineScope = SingleThreadCoroutineScope("Local Api Implementation Thread")
    val activeCalls = ArrayList<Job>()

    fun simulateCall(methodPath: MethodPath, parameterList: ParameterList, onResponse: Response.() -> Unit) {
        activeCalls.add(coroutineScope.launch {
            api.call(methodPath, constructCall(object : Session {
                override fun respond(response: Response) {
                    response.onResponse()
                }
            }, parameterList))
        })
    }

    fun joinAllCalls() {
        runBlocking(coroutineScope.coroutineContext) {
            activeCalls.forEach { it.join() }
        }
    }
}

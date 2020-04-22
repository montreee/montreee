package amber.api.server

import amber.api.ParameterList
import amber.api.response.Response


interface Call {
    val session: Session
    val parameter: ParameterList
    fun respond(response: Response)
}

fun constructCall(session: Session, parameter: ParameterList): Call = CallImpl(session, parameter)

private data class CallImpl(override val session: Session, override val parameter: ParameterList) : Call {
    override fun respond(response: Response) = session.respond(response)
}

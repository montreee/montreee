package amber.api.server

import amber.api.response.Response

interface Session {
    fun respond(response: Response)
}

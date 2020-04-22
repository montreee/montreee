package amber.server

import amber.sevices.Service

class ServerService(val server: Server) : Service() {
    override fun onStart() {
        server.start()
    }

    override fun onStop() {
        server.stop()
    }
}

package amber.api.server.impl.bdtp

import amber.api.response.Response
import amber.api.response.TextResponse
import amber.api.server.Session
import amber.bdtp.MessageEvent
import amber.bdtp.frame
import amber.sync.Synchronized

class BdtpApiImplSession(val messageEvent: MessageEvent) : Session, Synchronized by Synchronized() {

    private var responded = false

    override fun respond(response: Response) = synchronized {
        if (responded) return@synchronized

        responded = true
        messageEvent.session.sendSync(frame {
            if (response is TextResponse) {
                content(response.text)
            } else {
                content(String(response.bytes))
            }
        })
        messageEvent.session.close()
    }
}

package amber.api

import amber.api.client.BdtpApiClient
import amber.api.response.TextResponse
import amber.api.server.api
import amber.api.server.impl.bdtp.BdtpApiImpl
import amber.api.server.method
import amber.bdtp.Engine
import amber.bdtp.impl.LocalImpl
import amber.coroutines.scope
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers

class LocalBdtpServerClientTest : AnnotationSpec() {

    @Test
    suspend fun `simple method call with response`() {
        var wasCalled = false
        var parameterWasSupplied = false

        val api = api {
            method("method") {
                wasCalled = true
                if (parameter["name"] == "value") parameterWasSupplied = true
                respond(TextResponse("response"))
            }
        }

        val serverBdtpEngine = Engine(Dispatchers.Default.scope())
        val apiImpl = BdtpApiImpl(api, serverBdtpEngine)

        val clientBdtpEngine = Engine(Dispatchers.Default.scope())
        var client: BdtpApiClient? = null

        val localBdtp = LocalImpl(serverBdtpEngine, clientBdtpEngine)

        clientBdtpEngine.onConnect {
            client = BdtpApiClient(this)
        }
        apiImpl.start()
        localBdtp.start()

        val result = client?.call("method", constructParameter { "name" to "value" })
        wasCalled shouldBe true
        parameterWasSupplied shouldBe true
        val stringResult = String(result!!.bytes)
        stringResult shouldBe "response"

        client?.disconnect()
        apiImpl.stop()
    }
}
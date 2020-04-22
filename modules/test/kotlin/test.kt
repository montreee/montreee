import amber.api.client.KtorWSBdtpApiClient
import amber.api.constructParameter
import amber.coroutines.scope
import amber.logging.Logger
import amber.logging.format.Formats
import amber.logging.receiver.SimpleConsoleLogReceiver
import amber.text.prettyXml
import kotlinx.coroutines.Dispatchers

private var logOutLastRender: StringBuilder = StringBuilder()
private val log = Logger("").apply {
    onLog {
        logOutLastRender.appendln(Formats.Json(this))
    }
}
//todo Remove

suspend fun main(vararg arg: String) {
    println("starting...")

    val host = if (arg.isNotEmpty() && arg[0].isNotBlank()) arg[0] else "localhost"
    KtorWSBdtpApiClient(
            host,
            8056,
            requestTimeout = -1,
            encryption = false,
            logger = Logger("Render test logger").apply {
                add(
                        SimpleConsoleLogReceiver(
                                formatter =
                                Formats.Minimal
                        )
                )
            },
            coroutineScope = Dispatchers.Default.scope()
    ).apply {
        connect()
        repeat(10) {
            call("api/renderer/update", constructParameter {
                "name" to "index.html"
                "data" to TestStuff.testPageInput()
            })
        }
        repeat(10) {
            call("api/renderer/render", constructParameter {
                "name" to "index.html"
            })
            //            Thread.sleep(100)
        }
        repeat(10) {
            print(String(call("api/renderer/result", constructParameter {
                "name" to "index.html"
            }).bytes).prettyXml())
        }
        disconnect()
    }
}

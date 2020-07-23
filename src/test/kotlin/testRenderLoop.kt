import amber.api.client.KtorWSBdtpApiClient
import amber.api.constructParameter
import amber.coroutines.scope
import amber.logging.LogLevel
import amber.logging.Logger
import amber.logging.format.Formats
import amber.logging.info
import amber.logging.receiver.SimpleConsoleLogReceiver
import kotlinx.coroutines.Dispatchers
import kotlin.system.measureNanoTime

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
                                formatter = Formats.Minimal,
                                level = LogLevel.INFO
                        )
                )
            },
            coroutineScope = Dispatchers.Default.scope()
    ).apply { connect() }.apply {
        try {
            var lastResult = ""
            while (true) {
                var resultBytes: ByteArray = ByteArray(0)
                val update = measureNanoTime {
                    call("api/renderer/update", constructParameter {
                        "name" to "index.html"
                        "data" to TestStuff.testPageInput()
                    })
                } / 1000000F
                val render = measureNanoTime {
                    call("api/renderer/render", constructParameter {
                        "name" to "index.html"
                    })
                } / 1000000F
                val result = measureNanoTime {
                    resultBytes = call("api/renderer/result", constructParameter {
                        "name" to "index.html"
                    }).bytes
                } / 1000000F

                logger.info(
                        "rendered in ${"%.6f".format(update + render + result)} millis (" +
                        "update ${"%.6f".format(update)} millis, " +
                        "render ${"%.6f".format(render)} millis, " +
                        "result ${"%.6f".format(result)} millis" +
                        ")"
                )

                String(resultBytes).also {
                    assert(it.isNotBlank() && (it == lastResult || lastResult.isEmpty()))
                    lastResult = it
                }

                //runBlocking { delay(10) }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            disconnect()
        }
    }
}

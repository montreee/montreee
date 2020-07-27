import amber.logging.LogLevel
import amber.logging.LogReceiver
import amber.logging.Logger
import amber.logging.error
import amber.logging.format.Formats
import amber.logging.receiver.SimpleConsoleLogReceiver

fun main() {
    val main = Logger("main")
    val s1 = Logger("s-1", main)
    val s1s1 = Logger("s-1-1", s1)
    val s1s2 = Logger("s-1-2", s1)
    val s2 = Logger("s-2", main)
    val s2s1 = Logger("s-2-1", s2)
    val s2s2 = Logger("s-2-2", s2)

    fun log() {
        main.error {
            +"main"
            "key" to "value"
            "field" to "content"
        }

        s1.error {
            +"s-1"
            "key" to "value"
            "field" to "content"
        }

        s1s1.error {
            +"s-1-1"
            "key" to "value"
            "field" to "content"
        }

        s1s2.error {
            +"s-1-2"
            "key" to "value"
            "field" to "content"
        }

        s2.error {
            +"s-2"
            "key" to "value"
            "field" to "content"
        }

        s2s1.error {
            +"s-2-1"
            "key" to "value"
            "field" to "content"
        }

        s2s2.error {
            +"s-2-2"
            "key" to "value"
            "field" to "content"
        }
    }


    val receiver = listOf<LogReceiver>(
            SimpleConsoleLogReceiver(LogLevel.TRACE, Formats.Default),
            SimpleConsoleLogReceiver(LogLevel.TRACE, Formats.Flat),
            SimpleConsoleLogReceiver(LogLevel.TRACE, Formats.FlatWithPath),
            SimpleConsoleLogReceiver(LogLevel.TRACE, Formats.Minimal),
            SimpleConsoleLogReceiver(LogLevel.TRACE, Formats.MinimalWithPath),
            SimpleConsoleLogReceiver(LogLevel.TRACE, Formats.Json),
            SimpleConsoleLogReceiver(LogLevel.TRACE, Formats.PrettyJson)
    )

    receiver.forEachIndexed { i, e ->
        if (i > 0) main.remove(receiver[i - 1])
        main.add(e)
        log()
        println()
        println()
        println()
        println()
    }

}

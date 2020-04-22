package amber.logging.format

import amber.logging.Log
import amber.time.toTimeStamp
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectSerializer
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray

object Formats {

    object Default : Formatter {
        override fun Log.format(): String = buildString {
            append("${time.toTimeStamp()} ${level.name} [${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(": $message")
            if (childes.isEmpty()) return@buildString
            append(
                    buildString {
                        childes.forEach {
                            append("\n")
                            append(it.format())
                        }
                    }.prependIndent()
            )
        }
    }

    object Flat : Formatter {
        override fun Log.format(): String = buildString {
            append("${time.toTimeStamp()} ${level.name} [${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(": $message")
            if (childes.isEmpty()) return@buildString
            childes.forEach {
                append("\n")
                append(it.format())
            }
        }
    }

    object FlatWithPath : Formatter {
        override fun Log.format(): String = formatWithLoggerPrefix()
        private fun Log.formatWithLoggerPrefix(prefix: String = ""): String = buildString {
            append("${time.toTimeStamp()} ${level.name} [$prefix${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(": $message")
            if (childes.isEmpty()) return@buildString
            childes.forEach {
                append("\n")
                append(it.formatWithLoggerPrefix("$prefix${logger.name}|"))
            }
        }
    }

    object Minimal : Formatter {
        override fun Log.format(): String = buildString {
            if (message.isBlank() && fields.isEmpty()) {
                if (childes.isEmpty()) return@buildString
                childes.forEachIndexed { i, it ->
                    if (i > 0) append("\n")
                    append(it.format())
                }
                return@buildString
            }
            append("${time.toTimeStamp()} ${level.name} [${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(": $message")
            if (childes.isEmpty()) return@buildString
            childes.forEach {
                append("\n")
                append(it.format())
            }
        }
    }

    object MinimalWithPath : Formatter {
        override fun Log.format(): String = formatWithLoggerPrefix()
        private fun Log.formatWithLoggerPrefix(prefix: String = ""): String = buildString {
            if (message.isBlank() && fields.isEmpty()) {
                if (childes.isEmpty()) return@buildString
                childes.forEachIndexed { i, it ->
                    if (i > 0) append("\n")
                    append(it.formatWithLoggerPrefix("$prefix${logger.name}|"))
                }
                return@buildString
            }
            append("${time.toTimeStamp()} ${level.name} [$prefix${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(": $message")
            if (childes.isEmpty()) return@buildString
            childes.forEach {
                append("\n")
                append(it.formatWithLoggerPrefix("$prefix${logger.name}|"))
            }
        }
    }

    object Json : Formatter {
        override fun Log.format() = formatJson().toString()

        private fun Log.formatJson(): JsonObject = json {
            "level" to level.name
            "logger" to logger.name
            "time" to time.toTimeStamp()
            "message" to message
            if (childes.isNotEmpty()) {
                "childes" to jsonArray {
                    childes.forEach {
                        +it.formatJson()
                    }
                }
            }
            if (fields.isNotEmpty()) {
                "fields" to json {
                    fields.forEach {
                        it.key to it.value
                    }
                }
            }
        }
    }

    object PrettyJson : Formatter {
        override fun Log.format(): String {
            return kotlinx.serialization.json.Json.indented.stringify(JsonObjectSerializer, formatJson())
        }

        private fun Log.formatJson(): JsonObject = json {
            "level" to level.name
            "logger" to logger.name
            "time" to time.toTimeStamp()
            "message" to message
            if (childes.isNotEmpty()) {
                "childes" to jsonArray {
                    childes.forEach {
                        +it.formatJson()
                    }
                }
            }
            if (fields.isNotEmpty()) {
                "fields" to json {
                    fields.forEach {
                        it.key to it.value
                    }
                }
            }
        }
    }
}

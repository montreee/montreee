package amber.text

import kotlinx.serialization.json.*

fun String.replace(oldValues: List<String>, newValue: String): String {
    var ret = this
    oldValues.forEach {
        ret = ret.replace(it, newValue)
    }
    return ret
}

fun String.replace(oldValues: List<Char>, newValue: Char): String {
    val oldValuesAsString = arrayListOf<String>()
    oldValues.forEach {
        oldValuesAsString.add(it.toString())
    }
    return replace(oldValuesAsString, newValue.toString())
}

fun String.toJsonObject(): JsonObject? {
    return try {
        Json.parse(JsonObjectSerializer, this)
    } catch (e: Exception) {
        JsonObject(emptyMap())
    }
}

fun <T> String.toJsonArray(): JsonArray? {
    return try {
        Json.parse(JsonArraySerializer, this)
    } catch (e: Exception) {
        JsonArray(emptyList())
    }
}

const val ALPHANUMERIC_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

fun randomString(length: Int, chars: CharSequence = ALPHANUMERIC_CHARS): String = buildString {
    repeat(length) {
        append(chars.random())
    }
}

fun randomAlphanumericString(length: Int) = randomString(length, ALPHANUMERIC_CHARS)

fun String.random(length: Int) = randomString(length, this)

fun List<String>.joinToString(splitter: String): String {
    var ret = ""
    forEach {
        ret = ret.plusUseSplitterIfNotEmpty(it, splitter)
    }
    return ret
}

fun String.plusUseSplitterIfNotEmpty(toAdd: String, splitter: String): String =
        if (this != "") plusUseSplitter(toAdd, splitter) else this + toAdd

fun String.plusUseSplitter(toAdd: String, splitter: String): String = this + splitter + toAdd

fun String.repeat(stack: Int): String {
    return buildString {
        for (i in 0 until stack) {
            append(this@repeat)
        }
    }
}

fun String.prettyXml(indent: String = "  "): String {

    if (this.trim { it <= ' ' }.length == 0) return ""

    var stack = 0
    val rows = this.trim { it <= ' ' }.replace(">".toRegex(), ">\n").replace("<".toRegex(), "\n<").split("\n".toRegex())
            .dropLastWhile { it.isEmpty() }.toTypedArray()

    return buildString {
        for (i in rows.indices) {
            if (rows[i].trim { it <= ' ' }.length == 0) continue

            val row = rows[i].trim { it <= ' ' }
            if (row.startsWith("<?")) {
                append(row + "\n")
            } else if (row.startsWith("</")) {
                append(indent.repeat(--stack) + row + "\n")
            } else if (row.startsWith("<") && !row.endsWith("/>")) {
                append(indent.repeat(stack++) + row + "\n")
                if (row.endsWith("]]>")) stack--
            } else {
                append(indent.repeat(stack) + row + "\n")
            }
        }
    }.trim { it <= ' ' }
}

fun String.cut(vararg at: Char): List<String> {
    val indexes = mutableListOf<Int>()
    asIterable().forEachIndexed { i, char ->
        if (run {
                    at.forEach {
                        if (char == it) return@run true
                    }
                    return@run false
                }) {
            indexes.add(i)
        }
    }
    val result = mutableListOf<String>()
    var last = 0
    indexes.add(length)
    indexes.forEach {
        result.add(substring(last, it))
        last = it
    }
    return result
}

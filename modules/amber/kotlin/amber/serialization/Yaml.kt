package amber.serialization

import com.charleskorn.kaml.*

fun parseYaml(s: String): Map<String, Any?> {
    val parser = YamlParser(s)
    val reader = YamlNodeReader(parser, null)
    return (reader.read() as YamlMap).normalize()
}

fun YamlMap.normalize(): Map<String, Any?> {
    return mutableMapOf<String, Any?>().apply {
        this@normalize.entries.forEach {
            put(it.key.normalize() as String, it.value.normalize())
        }
    }
}

fun YamlList.normalize(): List<Any?> {
    return mutableListOf<Any?>().apply {
        this@normalize.items.forEach {
            add(it.normalize())
        }
    }
}

fun YamlNode.normalize(): Any? = when (this) {
    is YamlMap    -> normalize()
    is YamlScalar -> {
        var result: Any? = null

        fun setResultIfNull(block: () -> Any?) {
            if (result == null) {
                try {
                    result = block()
                } catch (e: YamlScalarFormatException) {
                }
            }
        }

        setResultIfNull { toLong() }
        setResultIfNull { toDouble() }
        setResultIfNull { toBoolean() }
        setResultIfNull { content }

        result
    }
    is YamlNull   -> null
    is YamlList   -> normalize()
    else          -> null
}

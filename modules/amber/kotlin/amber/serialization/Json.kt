package amber.serialization

import kotlinx.serialization.json.*

fun parseJson(s: String) = Json.parse(JsonObjectSerializer, s).normalize()

fun JsonObject.normalize(): Map<String, Any?> {
    return mutableMapOf<String, Any?>().apply {
        content.forEach {
            put(it.key, it.value.normalize())
        }
    }
}

fun JsonArray.normalize(): List<Any?> {
    return mutableListOf<Any?>().apply {
        this@normalize.forEach {
            add(it.normalize())
        }
    }
}

fun JsonElement.normalize(): Any? = when (this) {
    is JsonObject    -> normalize()
    is JsonPrimitive -> {
        var result: Any? = null

        fun setResultIfNull(block: () -> Any?) {
            if (result == null) {
                result = block()
            }
        }

        setResultIfNull { longOrNull }
        setResultIfNull { doubleOrNull }
        setResultIfNull { booleanOrNull }
        setResultIfNull { contentOrNull }

        result
    }
    is JsonNull      -> null
    is JsonArray     -> normalize()
}

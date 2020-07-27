package amber.io

import kotlin.reflect.KClass

class Resource(val path: String, private val context: KClass<*>? = null) {
    private val pre: String = if (!path.startsWith("/")) "/" else ""

    val url = (context ?: this::class).java.getResource(pre + path)
    val uri = url?.toURI()
    var externalForm = url?.toExternalForm()
    var stream = (context ?: this::class).java.getResourceAsStream(pre + path)

    fun readText() = url.readText()
}


package amber.source.provider

import amber.io.Resource
import amber.source.Source

class ResourceContentSource(private val resourcePath: String) : Source<String> {
    override fun read(): String {
        return Resource(resourcePath).readText()
    }
}

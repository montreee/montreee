package amber.config.impl

import amber.serialization.parseJson
import kotlinx.serialization.ImplicitReflectionSerializer

open class JsonConfig() : TreeMapConfig() {

    constructor(string: String) : this() {
        load(string)
    }

    @OptIn(ImplicitReflectionSerializer::class)
    fun load(string: String) {
        map = parseJson(string)
    }
}

package amber.config.impl

import amber.serialization.parseYaml
import kotlinx.serialization.ImplicitReflectionSerializer

open class YamlConfig() : TreeMapConfig() {

    constructor(string: String) : this() {
        load(string)
    }

    @OptIn(ImplicitReflectionSerializer::class)
    fun load(string: String) {
        map = parseYaml(string)
    }
}

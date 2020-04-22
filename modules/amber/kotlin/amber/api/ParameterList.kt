package amber.api

class ParameterList : MutableMap<String, String> by mutableMapOf() {
    infix fun String.to(it: String) = put(this, it)
}

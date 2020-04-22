package montreee.parameter

class ParameterStore(private val backend: Map<String, Parameter<*>> = mutableMapOf()) {

    inline operator fun <reified T> invoke(key: String, default: T): T {
        val result = get(key)
        return if (result is T) result else default
    }

    operator fun get(key: String): Parameter<*>? = backend.get(key)

}

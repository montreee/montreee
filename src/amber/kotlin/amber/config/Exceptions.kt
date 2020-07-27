package amber.config

open class ConfigException(message: String, cause: Throwable? = null) : Exception(message, cause)

class IsNotConfiguredException(path: String) : ConfigException("no value at \"$path\"")

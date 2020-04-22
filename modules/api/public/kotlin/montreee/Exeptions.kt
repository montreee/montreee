package montreee

open class MontreeeApiException(message: String = "", cause: Throwable? = null) : Exception(message, cause)

open class ParsingException(message: String = "", cause: Throwable? = null) : MontreeeApiException(message, cause)

open class RenderException(message: String = "", cause: Throwable? = null) : MontreeeApiException(message, cause)

open class FeatureException(message: String = "", cause: Throwable? = null) : MontreeeApiException(message, cause)

package amber.bdtp

class BdtpError(val code: Int, override val message: String? = null) : Exception()

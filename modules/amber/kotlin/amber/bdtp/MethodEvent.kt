package amber.bdtp

data class MethodEvent(
        val raw: Frame,
        val connection: Connection? = null,
        val session: Session? = null,
        val message: Message? = null,
        val method: Method
)
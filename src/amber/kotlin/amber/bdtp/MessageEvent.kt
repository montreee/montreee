package amber.bdtp

data class MessageEvent(val raw: Frame, val connection: Connection, val session: Session, val message: Message)
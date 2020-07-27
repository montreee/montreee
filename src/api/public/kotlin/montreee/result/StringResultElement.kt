package montreee.result

class StringResultElement(private val value: String) : ResultElement() {
    override fun read() = value
}

package amber.api.response

open class TextResponse(text: String) : Response() {
    var text: String
        set(value) {
            bytes = value.toByteArray()
        }
        get() = String(bytes)

    init {
        this.text = text
    }
}

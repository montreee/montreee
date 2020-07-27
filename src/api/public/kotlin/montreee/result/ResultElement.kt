package montreee.result

abstract class ResultElement {
    abstract fun read(): String
    override fun toString() = read()
}

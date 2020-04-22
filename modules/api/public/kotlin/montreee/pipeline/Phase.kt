package montreee.pipeline

interface Phase<C> {
    fun run(context: C)
}

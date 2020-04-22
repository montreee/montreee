package montreee.result

class ResultList(impl: MutableList<ResultElement>) : ResultElement(), MutableList<ResultElement> by impl {
    override fun read(): String {
        return buildString {
            this@ResultList.forEach {
                append(it)
            }
        }
    }
}

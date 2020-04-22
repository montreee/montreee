package amber.collections

fun <T> MutableList<T>.randomItem() = shuffled().last()

fun <T> Iterable<T>.randomItem() = shuffled().last()

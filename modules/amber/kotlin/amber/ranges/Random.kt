package amber.ranges

fun IntRange.random() = (Math.random() * ((endInclusive + 1) - start) + start).toInt()
fun LongRange.random() = (Math.random() * ((endInclusive + 1) - start) + start).toInt()

package amber.pipeline

class LambdaPhase<C>(private val block: suspend C.() -> Unit) : Phase<C> {
    override suspend fun run(context: C) {
        context.block()
    }
}

fun <C> phase(block: suspend C.() -> Unit): Phase<C> {
    return LambdaPhase<C>(block)
}

fun <C> Pipeline<C>.addAll(vararg phase: Phase<C>) {
    phase.forEach { add(it) }
}

operator fun <C> Pipeline<C>.invoke(block: suspend C.() -> Unit) = intercept(block)

fun <C> Pipeline<C>.intercept(block: suspend C.() -> Unit) = interceptAfter(block)

fun <C> Pipeline<C>.interceptAfter(block: suspend C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    add(phase)
    return phase
}

fun <C> Pipeline<C>.interceptBefore(block: suspend C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    push(phase)
    return phase
}

fun <C> Pipeline<C>.interceptAfter(after: Phase<C>, block: suspend C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    addAfter(after, phase)
    return phase
}

fun <C> Pipeline<C>.interceptBefore(before: Phase<C>, block: suspend C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    addBefore(before, phase)
    return phase
}

package montreee.pipeline

class LambdaPhase<C>(private val block: C.() -> Unit) : Phase<C> {
    override fun run(context: C) {
        context.block()
    }
}

fun <C> phase(block: C.() -> Unit): Phase<C> {
    return LambdaPhase<C>(block)
}

fun <C> Pipeline<C>.addAll(vararg phase: Phase<C>) {
    phase.forEach { add(it) }
}

operator fun <C> Pipeline<C>.invoke(block: C.() -> Unit) = intercept(block)

fun <C> Pipeline<C>.intercept(block: C.() -> Unit) = interceptAfter(block)

fun <C> Pipeline<C>.interceptAfter(block: C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    add(phase)
    return phase
}

fun <C> Pipeline<C>.interceptBefore(block: C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    push(phase)
    return phase
}

fun <C> Pipeline<C>.interceptAfter(after: Phase<C>, block: C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    addAfter(after, phase)
    return phase
}

fun <C> Pipeline<C>.interceptBefore(before: Phase<C>, block: C.() -> Unit): Phase<C> {
    val phase = phase<C>(block)
    addBefore(before, phase)
    return phase
}

package amber.factory

class LambdaFactory<T>(private val block: () -> T) : Factory<T> {
    override fun invoke() = block()
}

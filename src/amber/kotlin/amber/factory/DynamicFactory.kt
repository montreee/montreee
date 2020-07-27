package amber.factory

interface DynamicFactory<P, T> {
    operator fun invoke(parameter: P): T
}

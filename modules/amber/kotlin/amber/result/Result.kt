package amber.result

sealed class Result<V, R> {

    abstract val reports: List<R>

    data class Success<V, R>(
            val value: V, override val reports: List<R> = listOf()
    ) : Result<V, R>() {

        constructor(value: V, vararg reports: R) : this(value, reports.asList())
    }

    data class Failure<V, R>(
            override val reports: List<R>
    ) : Result<V, R>() {

        constructor(vararg reports: R) : this(reports.asList())
    }
}

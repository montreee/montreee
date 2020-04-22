package amber.result

inline fun <V, R> Result<V, R>.onSuccess(body: Result.Success<V, R>.() -> Unit): Result<V, R> {
    if (this is Result.Success) {
        this.body()
    }
    return this
}

inline fun <V, R> Result<V, R>.onFailure(body: Result.Failure<V, R>.() -> Unit): Result<V, R> {
    if (this is Result.Failure) {
        this.body()
    }
    return this
}

fun <V, R> V.asSuccess(reports: List<R> = listOf()): Result.Success<V, R> = createSuccess(this, reports)

fun <V, R> createSuccess(value: V, reports: List<R> = listOf()) = Result.Success(value, reports)

fun <V, R> createFailure(reports: List<R> = listOf()) = Result.Failure<V, R>(reports)

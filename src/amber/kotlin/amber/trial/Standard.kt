package amber.trial

fun <T> trial(block: () -> T): Trial<T> {
    val trial = Trial<T>(block)
    trial.execute()
    return trial
}

suspend fun <T> trialAsync(block: suspend () -> T): AsyncTrial<T> {
    val asyncTrial = AsyncTrial<T>(block)
    asyncTrial.execute()
    return asyncTrial
}

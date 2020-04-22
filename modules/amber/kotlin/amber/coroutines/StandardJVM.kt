package amber.coroutines

val parallelism by lazy { Runtime.getRuntime().availableProcessors() * 3 / 2 }

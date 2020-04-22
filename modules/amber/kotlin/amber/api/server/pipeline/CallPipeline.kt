package amber.api.server.pipeline

import amber.pipeline.Pipeline

class CallPipeline : Pipeline<CallContext>() {

    val setup = Pipeline<CallContext>()
    val monitoring = Pipeline<CallContext>()
    val features = Pipeline<CallContext>()
    val call = Pipeline<CallContext>()
    val fallback = Pipeline<CallContext>()

    init {
        +setup
        +monitoring
        +features
        +call
        +fallback
    }
}

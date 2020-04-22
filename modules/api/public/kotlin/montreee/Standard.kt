package montreee

import montreee.pipeline.Pipeline
import montreee.pipeline.intercept

operator fun <C> Pipeline<C>.invoke(block: C.() -> Unit) = intercept(block)

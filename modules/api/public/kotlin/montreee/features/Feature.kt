package montreee.features

import montreee.FeatureException
import montreee.context.Context

abstract class Feature {
    private fun throwNotInitializedError(): Nothing =
            throw FeatureException("Feature has to be initialized before using any properties.")

    private var _context: Context? = null
    protected val context
        get() = _context ?: throwNotInitializedError()

    fun init(context: Context) {
        _context = context
    }

    abstract fun install()
}

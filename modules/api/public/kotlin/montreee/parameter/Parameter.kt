package montreee.parameter

import montreee.ParsingException
import montreee.context.Context
import montreee.page.Page
import kotlin.reflect.KProperty

open class Parameter<T>(private val parameterLoader: ParameterLoader<T>) {

    val value by this

    private var _value: T? = null

    fun load(): T {
        _value = parameterLoader.load()
        return value
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _value ?: throw ParsingException("Parameter not loaded.")
    }

    fun init(context: Context, page: Page, input: Any?) = parameterLoader.init(context, page, input)

    override fun toString(): String {
        return "Parameter($value)"
    }
}

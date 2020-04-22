package montreee.parameter

import montreee.RenderException
import montreee.descriptor.InputType
import montreee.invoke
import montreee.module.ModuleLogic
import kotlin.reflect.KProperty

fun <T> ModuleLogic.parameter(
        key: String, inputType: InputType, isOptional: Boolean = false, default: T? = null
): ParameterProperty<T> {
    return ParameterProperty<T>(this, key, inputType, isOptional, default)
}

class ParameterProperty<T> internal constructor(
        private val module: ModuleLogic,
        private val key: String,
        private val inputType: InputType,
        private val isOptional: Boolean = false,
        private val default: T? = null
) {

    private var value: Parameter<T>? = null
    private var isSetupDone = false

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!isSetupDone) throw RenderException("Using this parameter is only allowed in render, feature and final.")
        return (value?.value ?: default) ?: throw RenderException("No value for parameter \"$key\" available.")
    }

    init {
        module.apply {
            info {
                require(key, inputType, isOptional)
            }
            setup {
                try {
                    @Suppress("UNCHECKED_CAST") value = parameter[key] as Parameter<T>
                } catch (e: TypeCastException) {
                }
                isSetupDone = true
            }
        }
    }
}

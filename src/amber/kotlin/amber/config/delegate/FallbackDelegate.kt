package amber.config.delegate

import amber.config.Delegate
import amber.trial.trial
import kotlin.reflect.KProperty

open class FallbackDelegate<T>(
        private val delegate: Delegate<T>, private val fallback: Delegate<T>
) : Delegate<T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = trial {
        delegate.getValue(thisRef, property)
    } alternate {
        fallback.getValue(thisRef, property)
    }
}

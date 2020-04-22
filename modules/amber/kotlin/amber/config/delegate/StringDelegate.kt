package amber.config.delegate

import amber.config.Config
import amber.config.Delegate
import amber.config.IsNotConfiguredException
import amber.trial.trial
import kotlin.reflect.KProperty

open class StringDelegate(private val config: Config, private val path: String = "") :
        Delegate<String> {
    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = trial {
        config[(if (path.isBlank()) property.name else path)]
    } alternate { throw IsNotConfiguredException((if (path.isBlank()) property.name else path)) }
}

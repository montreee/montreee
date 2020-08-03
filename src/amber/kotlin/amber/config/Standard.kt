package amber.config

import amber.config.delegate.BooleanDelegate
import amber.config.delegate.FallbackDelegate
import amber.config.delegate.NumberDelegate
import amber.config.delegate.StringDelegate
import amber.config.impl.SubConfig

fun Config.value(path: String = "") = StringDelegate(this, path)

fun Config.string(path: String = "") = value(path)

fun Config.number(path: String = "") = NumberDelegate(this, path)

fun Config.boolean(path: String = "") = BooleanDelegate(this, path)

fun <T> fallback(delegate: Delegate<T>, fallback: Delegate<T>) = FallbackDelegate(delegate, fallback)

fun Config.sub(path: String) = SubConfig(path, this)

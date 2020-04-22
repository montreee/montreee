package amber.config

import amber.trial.trial

interface Config {
    fun find(path: String): String?
    operator fun get(path: String): String = trial { find(path)!! }.alternate { throw IsNotConfiguredException(path) }
}

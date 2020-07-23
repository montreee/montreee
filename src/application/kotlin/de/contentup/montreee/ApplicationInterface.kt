package de.contentup.montreee

import amber.config.Config
import amber.logging.Logger

interface ApplicationInterface {
    val name: String
    val version: Version
    val logger: Logger
    val config: Config
    val context: Context
    fun launch() {}
    fun exit() {}
}

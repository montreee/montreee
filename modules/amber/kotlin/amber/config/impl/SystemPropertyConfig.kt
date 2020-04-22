package amber.config.impl

import amber.config.Config

class SystemPropertyConfig : Config {
    override fun find(path: String): String? {
        return System.getProperty(path)
    }
}

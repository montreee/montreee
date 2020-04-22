package amber.config.impl

import amber.config.Config

class EnvironmentVariablesConfig : Config {

    private val env get() = System.getenv()

    override fun find(path: String): String? {
        val env = env
        listOf(
                path,
                path.replace(".", "_").replace("-", "_").toUpperCase()
        ).forEach {
            if (env.containsKey(it)) return@find env[it]
        }
        return null
    }
}

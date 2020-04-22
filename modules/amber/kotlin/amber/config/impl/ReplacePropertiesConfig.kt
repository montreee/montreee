package amber.config.impl

import amber.config.Config
import amber.regex.regex

open class ReplacePropertiesConfig(private val impl: Config) : Config {

    private val replaceablePropertyRegex = regex {
        literally("$")
        literally("{")
        oneOrMore { anyOf({ alphaNumeric() }, { literally(".") }) }
        literally("}")
    }

    override fun find(path: String): String? {
        var ret = impl.get(path)
        replaceablePropertyRegex.findAll(ret).forEach {
            val replaceWith = get(it.value.substringAfter("\${").substringBefore("}"))
            ret = ret.replace(it.value, replaceWith)
        }
        return ret
    }
}

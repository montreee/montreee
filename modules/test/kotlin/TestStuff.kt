import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray

//todo Remove
object TestStuff {

    private fun generateX(i: Int): JsonObject {
        val moduleKey = "module"
        val parameterKey = "parameter"
        return when {
            i <= 0 -> json {
                moduleKey to "TestTextModule"
                parameterKey to json {
                    "text#String" to "yes yes yes\\°"
                }
            }
            i == 1 -> json {
                moduleKey to "ListModule"
                parameterKey to json {
                    "list#ModuleList" to jsonArray {
                        repeat(5) {
                            +generateX(0)
                        }
                    }
                }
            }
            else   -> json {
                moduleKey to "TestModule"
                parameterKey to json {
                    "subModule#Module" to generateX(i - 1)
                    "number#Number" to 42
                }
            }
        }
    }

    fun testPageInput() = generateX(200).toString()
}
package amber.config.impl

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import util.testString

class `JsonConfig - class` : AnnotationSpec() {

    @Test
    fun `load - function`() {
        val config = JsonConfig()
        val keys = mutableListOf<String>().apply { repeat(10) { add(testString()) } }
        val value = testString()
        val json = run {
            var iterator = keys.reversed().iterator()
            var current: JsonObject = json { iterator.next() to value }
            iterator.forEachRemaining { current = json { it to current } }
            current
        }.toString()

        config.load(json)

        config[keys.joinToString(".")] shouldBe value
    }
}

package amber.config.impl

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import util.testString

class `YamlConfig - class` : AnnotationSpec() {

    @Test
    fun `load - function`() {
        val config = YamlConfig()
        val keys = mutableListOf<String>().apply { repeat(10) { add(testString()) } }
        val value = testString()
        val yaml = run {
            var iterator = keys.reversed().iterator()
            var current = "${iterator.next()}: \"$value\""
            iterator.forEachRemaining { current = "${iterator.next()}: \n${current.prependIndent("  ")}" }
            current
        }

        config.load(yaml)

        config[keys.joinToString(".")] shouldBe value
    }
}

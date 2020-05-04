package amber.config.impl

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk
import util.testString
import util.testStringLower
import util.testStringUpper

class `EnvironmentVariablesConfig - class` : AnnotationSpec() {

    @Test
    fun `find - function`() {
        val env = mutableMapOf(
                testStringLower() to testString(),
                testStringUpper() to testString(),
                "${testStringLower()}.${testStringLower()}" to testString(),
                "${testStringUpper()}_${testStringUpper()}" to testString(),
                "${testStringUpper()}-${testStringUpper()}.${testStringUpper()}-${testStringUpper()}" to testString(),
                "${testStringUpper()}_${testStringUpper()}_${testStringUpper()}_${testStringUpper()}" to testString()
        )
        val envList = env.entries.toList()

        val config: EnvironmentVariablesConfig = spyk(EnvironmentVariablesConfig())
        every { config getProperty "env" } returns env

        config.find(envList[0].key) shouldBe envList[0].value
        config.find(envList[1].key) shouldBe envList[1].value
        config.find(envList[2].key) shouldBe envList[2].value
        config.find(envList[3].key.replace("_", ".")) shouldBe envList[3].value
        config.find(envList[4].key) shouldBe envList[4].value
        config.find(
                envList[5].key.replaceFirst("_", "-").replaceFirst("_", ".").replaceFirst("_", "-")
        ) shouldBe envList[5].value
    }
}

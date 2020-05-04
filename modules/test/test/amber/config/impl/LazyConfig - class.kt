package amber.config.impl

import amber.config.Config
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import util.testString

class `LazyConfig - class` : AnnotationSpec() {

    @Test
    fun `lazy initialization of config - integration`() {
        val key = testString()
        val value = testString()

        val configMock = mockk<Config>()
        every { configMock.find(key) } returns value

        var initialized = false
        val config = LazyConfig {
            initialized = true
            configMock
        }

        verify(exactly = 0) { configMock.find(key) }
        initialized shouldBe false

        config.find(key) shouldBe value

        verify(exactly = 1) { configMock.find(key) }
        initialized shouldBe true
    }
}

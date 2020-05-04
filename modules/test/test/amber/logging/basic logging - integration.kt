package amber.logging

import amber.logging.format.Formats
import amber.logging.util.stackTraceAsSting
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldHaveSize
import util.testString

class `basic logging - integration` : AnnotationSpec() {

    private val logs = mutableListOf<String>()
    private val logger = Logger(testString()).apply {
        onLog {
            logs.add(Formats.Default(this))
        }
    }

    @BeforeEach
    fun prepare() {
        logs.clear()
    }

    @Test
    fun `basic logging with level error - integration`() {
        val log1 = testString()
        logger.error(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.error { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("error", true) } shouldHaveSize 2

        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `basic logging with level warn - integration`() {
        val log1 = testString()
        logger.warn(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.warn { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("warn", true) } shouldHaveSize 2

        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `basic logging with level info - integration`() {
        val log1 = testString()
        logger.info(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.info { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("info", true) } shouldHaveSize 2

        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `basic logging with level debug - integration`() {
        val log1 = testString()
        logger.debug(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.debug { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("debug", true) } shouldHaveSize 2

        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `basic logging with level trace - integration`() {
        val log1 = testString()
        logger.trace(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.trace { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("trace", true) } shouldHaveSize 2

        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `basic logging with exception - integration`() {
        val exceptionMessage = testString()
        val exception = Exception(exceptionMessage)
        logger.error(exception)
        logs.filter { it.contains(exception.stackTraceAsSting()) } shouldHaveSize 1

        logs.filter { it.contains("error", true) } shouldHaveSize 1

        logs.filter { it.contains(logger.name) } shouldHaveSize 1

        logs shouldHaveSize 1
    }
}

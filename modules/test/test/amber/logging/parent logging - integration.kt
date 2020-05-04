package amber.logging

import amber.logging.format.Formats
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldHaveSize
import util.testString

class `parent logging - integration` : AnnotationSpec() {

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
    fun `parent logging with level error - integration`() {
        val parent1 = logger
        val parent2 = Logger(testString(), parent1)
        val parent3 = Logger(testString(), parent2)
        val parent4 = Logger(testString(), parent3)
        val logger = Logger(testString(), parent4)

        val log1 = testString()
        logger.error(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.error { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("error", true) } shouldHaveSize 2

        logs.filter { it.contains(parent1.name) } shouldHaveSize 2
        logs.filter { it.contains(parent2.name) } shouldHaveSize 2
        logs.filter { it.contains(parent3.name) } shouldHaveSize 2
        logs.filter { it.contains(parent4.name) } shouldHaveSize 2
        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `parent logging with level warn - integration`() {
        val parent1 = logger
        val parent2 = Logger(testString(), parent1)
        val parent3 = Logger(testString(), parent2)
        val parent4 = Logger(testString(), parent3)
        val logger = Logger(testString(), parent4)

        val log1 = testString()
        logger.warn(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.warn { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("warn", true) } shouldHaveSize 2

        logs.filter { it.contains(parent1.name) } shouldHaveSize 2
        logs.filter { it.contains(parent2.name) } shouldHaveSize 2
        logs.filter { it.contains(parent3.name) } shouldHaveSize 2
        logs.filter { it.contains(parent4.name) } shouldHaveSize 2
        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `parent logging with level info - integration`() {
        val parent1 = logger
        val parent2 = Logger(testString(), parent1)
        val parent3 = Logger(testString(), parent2)
        val parent4 = Logger(testString(), parent3)
        val logger = Logger(testString(), parent4)

        val log1 = testString()
        logger.info(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.info { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("info", true) } shouldHaveSize 2

        logs.filter { it.contains(parent1.name) } shouldHaveSize 2
        logs.filter { it.contains(parent2.name) } shouldHaveSize 2
        logs.filter { it.contains(parent3.name) } shouldHaveSize 2
        logs.filter { it.contains(parent4.name) } shouldHaveSize 2
        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `parent logging with level debug - integration`() {
        val parent1 = logger
        val parent2 = Logger(testString(), parent1)
        val parent3 = Logger(testString(), parent2)
        val parent4 = Logger(testString(), parent3)
        val logger = Logger(testString(), parent4)

        val log1 = testString()
        logger.debug(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.debug { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("debug", true) } shouldHaveSize 2

        logs.filter { it.contains(parent1.name) } shouldHaveSize 2
        logs.filter { it.contains(parent2.name) } shouldHaveSize 2
        logs.filter { it.contains(parent3.name) } shouldHaveSize 2
        logs.filter { it.contains(parent4.name) } shouldHaveSize 2
        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

    @Test
    fun `parent logging with level trace - integration`() {
        val parent1 = logger
        val parent2 = Logger(testString(), parent1)
        val parent3 = Logger(testString(), parent2)
        val parent4 = Logger(testString(), parent3)
        val logger = Logger(testString(), parent4)

        val log1 = testString()
        logger.trace(log1)
        logs.filter { it.contains(log1) } shouldHaveSize 1

        val log2 = testString()
        logger.trace { +log2 }
        logs.filter { it.contains(log2) } shouldHaveSize 1

        logs.filter { it.contains("trace", true) } shouldHaveSize 2

        logs.filter { it.contains(parent1.name) } shouldHaveSize 2
        logs.filter { it.contains(parent2.name) } shouldHaveSize 2
        logs.filter { it.contains(parent3.name) } shouldHaveSize 2
        logs.filter { it.contains(parent4.name) } shouldHaveSize 2
        logs.filter { it.contains(logger.name) } shouldHaveSize 2

        logs shouldHaveSize 2
    }

}

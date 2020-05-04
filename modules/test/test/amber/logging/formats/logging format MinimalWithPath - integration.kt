package amber.logging.formats

import amber.logging.LazyLog
import amber.logging.Log
import amber.logging.LogLevel
import amber.logging.Logger
import amber.logging.format.Formats
import amber.time.toTimeStamp
import com.soywiz.klock.DateTime
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import util.testString

class `logging format MinimalWithPath - integration` : AnnotationSpec() {

    @Test
    fun `logging with format MinimalWithPath - integration`() {
        val logs = mutableListOf<String>()
        val logger = Logger(testString()).apply {
            onLog {
                logs.add(Formats.MinimalWithPath(this))
            }
        }
        val logsToTest = mutableListOf<Log>().apply {
            listOf(
                    LogLevel.ERROR,
                    LogLevel.WARN,
                    LogLevel.INFO,
                    LogLevel.DEBUG,
                    LogLevel.TRACE
            ).forEach {
                add(LazyLog(logger, it, DateTime.now()) {
                    +testString()
                    testString() to testString()
                    testString() to testString()
                })
            }
        }
        logsToTest.forEach {
            logs.clear()
            logger.log(it)
            logs shouldHaveSize 1
            val fieldsString = buildString {
                append(" { ")
                it.fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            logs.first() shouldBe "${it.time.toTimeStamp()} ${it.level} [${logger.name}]$fieldsString: ${it.message}"
        }
    }

    @Test
    fun `logging with format MinimalWithPath and parent logger - integration`() {
        val logs = mutableListOf<String>()
        val parent1 = Logger(testString()).apply {
            onLog {
                logs.add(Formats.MinimalWithPath(this))
            }
        }
        val parent2 = Logger(testString(), parent1, reuseTime = true)
        val parent3 = Logger(testString(), parent2, reuseTime = true)
        val logger = Logger(testString(), parent3, reuseTime = true)
        val logsToTest = mutableListOf<Log>().apply {
            listOf(
                    LogLevel.ERROR,
                    LogLevel.WARN,
                    LogLevel.INFO,
                    LogLevel.DEBUG,
                    LogLevel.TRACE
            ).forEach {
                add(LazyLog(logger, it, DateTime.now()) {
                    +testString()
                    testString() to testString()
                    testString() to testString()
                })
            }
        }
        logsToTest.forEach {
            logs.clear()
            logger.log(it)
            logs shouldHaveSize 1
            val fieldsString = buildString {
                append(" { ")
                it.fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            logs.first() shouldBe "${it.time.toTimeStamp()} ${it.level} " +
                    "[${parent1.name}|${parent2.name}|${parent3.name}|${logger.name}]$fieldsString: ${it.message}"
        }
    }
}

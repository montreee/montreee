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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObjectSerializer
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import util.testString

class `logging format PrettyJson - integration` : AnnotationSpec() {

    @Test
    fun `logging with format PrettyJson - integration`() {
        val logs = mutableListOf<String>()
        val logger = Logger(testString()).apply {
            onLog {
                logs.add(Formats.PrettyJson(this))
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
            logs.first() shouldBe Json.indented.stringify(JsonObjectSerializer, json {
                "level" to it.level.name
                "logger" to logger.name
                "time" to it.time.toTimeStamp()
                "message" to it.message
                "fields" to json {
                    it.fields.forEach {
                        it.key to it.value
                    }
                }
            })
        }
    }

    @Test
    fun `logging with format PrettyJson and parent logger - integration`() {
        val logs = mutableListOf<String>()
        val parent1 = Logger(testString()).apply {
            onLog {
                logs.add(Formats.PrettyJson(this))
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
            logs.first() shouldBe Json.indented.stringify(JsonObjectSerializer, json {
                "level" to it.level.name
                "logger" to parent1.name
                "time" to it.time.toTimeStamp()
                "message" to ""
                "childes" to jsonArray {
                    +json {
                        "level" to it.level.name
                        "logger" to parent2.name
                        "time" to it.time.toTimeStamp()
                        "message" to ""
                        "childes" to jsonArray {
                            +json {
                                "level" to it.level.name
                                "logger" to parent3.name
                                "time" to it.time.toTimeStamp()
                                "message" to ""
                                "childes" to jsonArray {
                                    +json {
                                        "level" to it.level.name
                                        "logger" to logger.name
                                        "time" to it.time.toTimeStamp()
                                        "message" to it.message
                                        "fields" to json {
                                            it.fields.forEach {
                                                it.key to it.value
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
    }
}

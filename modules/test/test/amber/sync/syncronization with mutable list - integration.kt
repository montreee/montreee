package amber.sync

import amber.coroutines.joinAllJobs
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope

class `syncronization with mutable list - integration` : AnnotationSpec() {

    private val factor = 50

    @Test
    suspend fun `suspending concurrent add - integration`() {
        coroutineScope {
            val list = mutableListOf<String>()
            val synchronized = Synchronized(SyncMode.SAFE_EXECUTOR)
            joinAllJobs {
                repeat(factor) {
                    GlobalScope.launch {
                        repeat(factor) {
                            synchronized.asyncSynchronized {
                                list.add(it.toString())
                            }
                        }
                    }
                }
            }
            list.size shouldBe factor * factor
            joinAllJobs {
                repeat(factor) {
                    GlobalScope.launch {
                        repeat(factor) {
                            synchronized.asyncSynchronized {
                                list.removeAt(list.size - 1)
                            }
                        }
                    }
                }
            }
            list.size shouldBe 0
        }
    }

    @Test
    suspend fun `concurrent add - integration`() {
        coroutineScope {
            val list = mutableListOf<String>()
            val synchronized = Synchronized(SyncMode.SAFE_EXECUTOR)
            joinAllJobs {
                repeat(factor) {
                    GlobalScope.launch {
                        repeat(factor) {
                            synchronized.synchronized {
                                list.add(it.toString())
                            }
                        }
                    }
                }
            }
            list.size shouldBe factor * factor
            joinAllJobs {
                repeat(factor) {
                    GlobalScope.launch {
                        repeat(factor) {
                            synchronized.synchronized {
                                list.removeAt(list.size - 1)
                            }
                        }
                    }
                }
            }
            list.size shouldBe 0
        }
    }

    @Test
    fun `non concurrent add - integration`() {
        val list = mutableListOf<String>()
        repeat(factor) {
            repeat(factor) {
                list.add(it.toString())
            }
        }

        list.size shouldBe factor * factor
        repeat(factor) {
            repeat(factor) {
                list.removeAt(list.size - 1)
            }
        }
        list.size shouldBe 0
    }
}

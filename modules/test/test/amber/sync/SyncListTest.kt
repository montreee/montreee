package amber.sync

import amber.coroutines.joinAllJobs
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope

class SyncListTest : AnnotationSpec() {

    private val factor = 50

    @Test
    suspend fun `suspending concurrent add`() {
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
    suspend fun `concurrent add`() {
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
    fun `non concurrent add`() {
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
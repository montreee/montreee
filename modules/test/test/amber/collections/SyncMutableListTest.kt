package amber.collections

import amber.coroutines.joinAllJobs
import amber.sync.SyncMode
import amber.sync.Synchronized
import io.kotlintest.forAll
import io.kotlintest.matchers.concurrent.shouldCompleteWithin
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import amber.collections.SyncMutableList as SyncList

class SyncMutableListTest : AnnotationSpec() {

    private suspend fun testWith(it: Int, b: SyncMode) {
        val factor = it
        val list = SyncList<String>(synchronized = Synchronized(b))
        coroutineScope<Unit> {
            this.joinAllJobs {
                repeat(factor) {
                    GlobalScope.launch {
                        repeat(factor) {
                            list.add("test")
                        }
                    }
                }
            }
        }
        list.size shouldBe factor * factor
        coroutineScope<Unit> {
            this.joinAllJobs {
                repeat(factor) {
                    GlobalScope.launch {
                        repeat(factor) {
                            list.synchronized<Unit> {
                                list.remove(list.last())
                            }
                        }
                    }
                }
            }
        }
        list.size shouldBe 0
    }

    @Test
    suspend fun `async add and remove SyncMode_UNSAFE_EXECUTOR`() {
        shouldCompleteWithin(10, TimeUnit.SECONDS) {
            table(
                    headers("factor", "syncMode"), row(arrayOf(10, 20, 30), SyncMode.UNSAFE_EXECUTOR)
            ).forAll { a, b ->
                forAll(a) {
                    runBlocking { testWith(it, b) }
                }
            }
        }
    }

    @Test
    suspend fun `async add and remove SyncMode_SAFE_EXECUTOR`() {
        shouldCompleteWithin(10, TimeUnit.SECONDS) {
            table(
                    headers("factor", "syncMode"), row(arrayOf(10, 20, 30), SyncMode.SAFE_EXECUTOR)
            ).forAll { a, b ->
                forAll(a) {
                    runBlocking { testWith(it, b) }
                }
            }
        }
    }

    @Test
    suspend fun `async add and remove SyncMode_HEAVY_EXECUTOR`() {
        shouldCompleteWithin(10, TimeUnit.SECONDS) {
            table(
                    headers("factor", "syncMode"), row(arrayOf(10, 20, 30), SyncMode.HEAVY_EXECUTOR)
            ).forAll { a, b ->
                forAll(a) {
                    runBlocking { testWith(it, b) }
                }
            }
        }
    }

    @Test
    suspend fun `async add and remove SyncMode_KOTLIN`() {
        shouldCompleteWithin(10, TimeUnit.SECONDS) {
            table(
                    headers("factor", "syncMode"), row(arrayOf(10, 20, 30, 100), SyncMode.KOTLIN)
            ).forAll { a, b ->
                forAll(a) {
                    runBlocking { testWith(it, b) }
                }
            }
        }
    }
}

package amber.collections

import amber.sync.Synchronized

fun List<*>.containsIndex(index: Int) = index in 0 until size

private data class MapEntry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V>

fun <K, V> mapEntryOf(key: K, value: V): Map.Entry<K, V> = MapEntry(key, value)


fun <T> MutableList<T>.copyAllValues(from: MutableList<T>) {
    for (i in 0 until from.size) {
        this.add(from[i])
    }
}

fun <T> MutableList<T>.deleteAllEqualValues(to: MutableList<T>) {
    val buffer = ArrayList<T>()
    buffer.copyAllValues(this)
    buffer.forEach {
        if (to.contains(it)) this.remove(it)
    }
}

fun <T> MutableList<T>.moveAllValues(to: MutableList<T>) {
    to.copyAllValues(this)
    deleteAllEqualValues(to)
}

inline fun <T> MutableList<T>.forEachSave(block: (T) -> Unit) {
    val valueCopy = mutableListOf<T>()
    valueCopy.copyAllValues(this)
    valueCopy.forEach {
        block(it)
    }
}

fun <T> syncListOf(vararg element: T): List<T> = SyncMutableList<T>(mutableListOf(*element), Synchronized())

fun <T> syncMutableListOf(vararg element: T) = SyncMutableList<T>(mutableListOf(*element), Synchronized())

fun <T> MutableList<T>.sync(synchronized: Synchronized = Synchronized()) = SyncMutableList<T>(this, synchronized)

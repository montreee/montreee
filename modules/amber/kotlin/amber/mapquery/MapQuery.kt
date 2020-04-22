package amber.mapquery

import amber.text.replace

fun <K, V> Map<K, V>.query() = MapQueryValue(this)
fun <T> List<T>.query() = MapQueryValue(this)

fun MapQueryValue.getByPath(path: String): MapQueryValue {
    var ret = this
    path.replace(listOf(":", ".", "/", "\\"), ":").split(":").forEach {
        ret = ret[it]
    }
    return ret
}

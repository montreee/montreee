package amber.api

import amber.text.replace

typealias MethodPath = String

fun MethodPath.normalize(): MethodPath {
    var ret = this
    ret = ret.replace(listOf("\\", ":", "."), "/")
    if (ret.startsWith("/")) ret = ret.substringAfter("/")
    if (ret.endsWith("/")) ret = ret.substringBeforeLast("/")

    return ret
}

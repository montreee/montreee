package de.contentup.montreee

import amber.time.toDateTime
import com.soywiz.klock.DateTime

fun versionFromString(string: String): Version {
    var parsed = ""
    fun currentString() = string.substringAfter(parsed)
    fun nextLine() = currentString().substringBefore("\n").apply { parsed += (this + "\n") }.removeSuffix("\r")

    val name = nextLine().substringAfter("Name: ")
    val versionString = nextLine().substringAfter("Version: ")
    val millennium = versionString.substringBefore(".")
    val release = versionString.substringAfter("$millennium.").substringBefore(".")
    val patch = versionString.substringAfter("$millennium.$release.").substringBefore("-")
    val build = nextLine().substringAfter("Build: ")
    val buildTime = nextLine().substringAfter("BuildTime: ").toDateTime()
    val info = currentString().substringAfter("Info: ")

    return Version(name, millennium.toInt(), release.toInt(), patch.toInt(), build, buildTime, info)
}

data class Version(
        val name: String = "",
        val millennium: Int = 0,
        val release: Int = 0,
        val patch: Int = 0,
        val build: String = "",
        val buildTime: DateTime = DateTime.now(),
        val info: String = ""
) {

    fun versionString() = "$millennium.$release.$patch"

}

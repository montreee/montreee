package de.contentup.montreee.cli

import amber.time.formatDefault
import de.contentup.montreee.cli.cmd.module.ModuleLoader

fun printInfo() {
    print(buildString {
        appendln(version.name)
        appendln("Version: ${version.versionString()}")
        appendln("Build: ${version.build}")
        appendln("BuildTime: ${version.buildTime.formatDefault()} UTC")
        appendln(version.info)
    })
}

fun printUseHelp() {
    println("Use --help to get information about how to use Montreee.")
}

fun printAvailableModules() {
    println("Modules:")
    ModuleLoader.moduleNames.forEach {
        println(it.joinToString(", ").prependIndent("  "))
    }
}

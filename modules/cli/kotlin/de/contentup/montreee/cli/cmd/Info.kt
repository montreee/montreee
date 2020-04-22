package de.contentup.montreee.cli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import de.contentup.montreee.cli.printInfo

class Info : CliktCommand(name = "info") {
    override fun run() {
        printInfo()
    }
}
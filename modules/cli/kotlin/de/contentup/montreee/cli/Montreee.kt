package de.contentup.montreee.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

import de.contentup.montreee.cli.cmd.Info
import de.contentup.montreee.cli.cmd.Module

class Montreee : CliktCommand(name = "montreee", printHelpOnEmptyArgs = false, invokeWithoutSubcommand = true) {

    init {
        subcommands(Info(), Module())
    }

    override fun run() {
        if (context.invokedSubcommand != null) return

        printInfo()
        printUseHelp()
    }
}
package de.contentup.montreee.cli.cmd

import amber.config.Config
import amber.config.impl.*
import amber.logging.LogLevel
import amber.logging.Logger
import amber.logging.error
import amber.logging.format.Formats
import amber.logging.info
import amber.logging.receiver.SimpleConsoleLogReceiver
import amber.trial.trial
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.pair
import com.github.ajalt.clikt.parameters.types.choice
import de.contentup.montreee.Application
import de.contentup.montreee.cli.cmd.module.ModuleLoader
import de.contentup.montreee.cli.printAvailableModules
import de.contentup.montreee.cli.version
import de.contentup.montreee.errorHandeling.LogErrorHandler
import java.io.File
import kotlin.concurrent.thread
import kotlin.system.exitProcess

private val montreeeLauncherLogger = Logger("Montreee CLI Logger")
private var consoleLogReceiver = SimpleConsoleLogReceiver(LogLevel.INFO, formatter = Formats.MinimalWithPath).apply {
    montreeeLauncherLogger.add(this)
}

class Module : CliktCommand(
        name = "module", help = "used to launch a specific montreee module", invokeWithoutSubcommand = true
) {

    val moduleName: String? by argument(
            "name", help = "name of the montreee module (if not specified default module will be launched)"
    ).default("default")

    val logLevel by option("--log", "-l").choice(
            "trace" to LogLevel.TRACE,
            "debug" to LogLevel.DEBUG,
            "info" to LogLevel.INFO,
            "warn" to LogLevel.WARN,
            "error" to LogLevel.ERROR
    ).default(LogLevel.INFO)

    val configArg: List<Pair<String, String>>? by option(
            "-c", "--configure", help = "sets a config value", metavar = "KEY VALUE"
    ).pair().multiple()


    val configs = mutableListOf<Config>().apply {
        add(LazyConfig { MapConfig(configArg?.toMap()?.toMutableMap() ?: mutableMapOf()) })

        add(PrefixedConfig("montreee", EnvironmentVariablesConfig()))

        val yamlConfigFile = File("config.yaml")
        if (yamlConfigFile.exists() && yamlConfigFile.canRead() && yamlConfigFile.isFile)
            add(YamlConfig(yamlConfigFile.readText()))

        val ymlConfigFile = File("config.yml")
        if (ymlConfigFile.exists() && ymlConfigFile.canRead() && ymlConfigFile.isFile)
            add(YamlConfig(ymlConfigFile.readText()))

        val jsonConfigFile = File("config.json")
        if (jsonConfigFile.exists() && jsonConfigFile.canRead() && jsonConfigFile.isFile)
            add(JsonConfig(jsonConfigFile.readText()))
    }

    private fun launch() {
        val oldConsoleLogReceiver = consoleLogReceiver
        consoleLogReceiver = SimpleConsoleLogReceiver(
                logLevel, Formats.MinimalWithPath
        ).apply { montreeeLauncherLogger.add(this) }
        montreeeLauncherLogger.remove(oldConsoleLogReceiver)

        val app = ModuleLoader.load(
                moduleName ?: ""
        ).invoke(version).apply {

            configs.forEach {
                config.load(it)
            }

            logger.add(consoleLogReceiver)
        }

        Application.events.onBeforeLaunch {
            Thread.setDefaultUncaughtExceptionHandler(LogErrorHandler(Logger("Uncaught Exception Logger", logger)))
            montreeeLauncherLogger.info("Launching $name")
        }
        Application.events.onBeforeExit {
            montreeeLauncherLogger.info("Closing $name")
        }

        Application.launch(app)
    }

    override fun run() {

        thread(name = "Montreee Launcher Thread") {

            try {

                launch()

            } catch (e: ModuleLoader.NoModuleFoundException) {
                println("No module with name \"${e.name}\" found.")
                println()

                printAvailableModules()
                exitProcess(2)
            } catch (e: ModuleLoader.MultipleModulesFoundException) {

                if (e.name.isNotEmpty()) {
                    println("Multiple modules matches \"${e.name}\". Please be more explicit.")
                    println()
                } else {
                    println("No module specified.")
                    println()
                }

                printAvailableModules()
                exitProcess(2)
            } catch (t: Throwable) {
                montreeeLauncherLogger.error(t)
            }
        }

        var shutdownHockExecuted = false

        val shutdownHock = thread(start = false, name = "Montreee Closer Thread", isDaemon = true) {
            thread(name = "Montreee Fallback Terminator Thread", isDaemon = true) {
                Thread.sleep(1500)
                System.exit(0)
            }
            trial { Application.exit() }
            shutdownHockExecuted = true
        }

        Runtime.getRuntime().addShutdownHook(shutdownHock)

        thread(name = "Montreee stay a live") {
            while (!shutdownHockExecuted) {
                Thread.sleep(100)
            }
        }
    }
}

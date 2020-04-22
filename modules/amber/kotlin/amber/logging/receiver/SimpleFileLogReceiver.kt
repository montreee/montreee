package amber.logging.receiver

import amber.io.createFileIfNotExists
import amber.io.createFolderIfNotExists
import amber.io.splintedFilePath
import amber.logging.Log
import amber.logging.LogLevel
import amber.logging.LogReceiver
import amber.logging.format.Formats
import amber.logging.format.Formatter
import amber.sync.Synchronized
import amber.text.replace
import amber.time.formatDefault
import com.soywiz.klock.DateTime
import java.io.File

class SimpleFileLogReceiver(
        var logsPerFile: Long,
        val logLevel: LogLevel,
        val logFolder: File = File("log"),
        val formatter: Formatter = Formats.Default
) : LogReceiver(), Synchronized by Synchronized() {

    var currentFile: File? = null
    var currentTime = DateTime.now()
    var logsInFile = 0

    init {
        logFolder.createFolderIfNotExists()
    }

    override fun onLog(log: Log) {
        synchronized {
            currentTime = DateTime.now()
            if (log.level.isGreaterOrEqual(logLevel)) {
                logsInFile++
                loadOrCreateCurrentFile()
                logInFile(log)
                if (logsInFile >= logsPerFile) {
                    endFile()
                }
            }
        }
    }

    private fun loadOrCreateCurrentFile() {
        if (currentFile == null) {
            findCurrentLogFile()
            if (currentFile == null) {
                createFile()
            }
        }
    }

    private fun findCurrentLogFile() {
        logFolder.listFiles()?.forEach {
            if (it.splintedFilePath().fullName.endsWith("now.log")) {
                currentFile = it
                it.readLines().forEach {
                    if (!it.startsWith(" ")) logsInFile++
                }
            }
        }
    }

    private fun createFile() {
        currentFile = File(generateFileNameNow())
        currentFile!!.createFileIfNotExists()
    }

    private fun generateFileNameNow(): String {
        return "${logFolder.absolutePath}/${currentTime.toFileNameSting()} - now.log"
    }

    private fun logInFile(log: Log) {
        var logString = formatter(log)
        if (!logString.endsWith("\n")) logString += "\n"
        currentFile!!.appendText(logString)
    }

    private fun endFile() {
        currentTime = DateTime.now()
        renameFile()
        currentFile = null
        logsInFile = 0
    }

    private fun renameFile() {
        val splitedFilePath = currentFile?.splintedFilePath()
        currentFile?.renameTo(
                File(
                        "${splitedFilePath?.directory}/${splitedFilePath?.fullName?.replaceAfterLast(
                                " - ", "${currentTime.toFileNameSting()}.log"
                        )}"
                )
        )
    }

    private fun DateTime.toFileNameSting() = formatDefault().replace(listOf(":", "."), "-")
}

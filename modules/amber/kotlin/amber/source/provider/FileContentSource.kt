package amber.source.provider

import amber.io.setupAsMutableFile
import amber.source.MutableSource
import java.io.File

class FileContentSource(private val file: File) : MutableSource<String> {

    constructor(path: String) : this(File(path))

    override fun write(t: String) {
        if (!file.setupAsMutableFile()) return

        file.writeText(t)
    }

    override fun read(): String {
        if (!file.setupAsMutableFile()) return ""

        return file.readText()
    }
}

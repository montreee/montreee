package amber.bdtp

import amber.text.cut

fun frame(block: Frame.ModifyScope.() -> Unit) = Frame(block = block)

fun frame(value: String): Frame {

    fun List<String>.contentOfHeadSection(maker: Char) = (find { it.startsWith(maker) } ?: "").replaceFirst(
            maker.toString(), ""
    )

    val head = value.substringBefore(Maker.content)


    val headSections = with(Maker) {
        head.cut(
                method, sessionId, messageId, packageNumber, totalPackages, path, contentType
        )
    }

    val method = headSections.contentOfHeadSection(Maker.method)
    val sessionId = headSections.contentOfHeadSection(Maker.sessionId)
    val messageId = headSections.contentOfHeadSection(Maker.messageId)
    val packageNumber = headSections.contentOfHeadSection(Maker.packageNumber)
    val numberOfTotalPackages = headSections.contentOfHeadSection(Maker.totalPackages)
    val path = headSections.contentOfHeadSection(Maker.path)
    val contentType = headSections.contentOfHeadSection(Maker.contentType)

    val content = value.substringAfter(Maker.content, "")

    return Frame(
            method, sessionId, messageId, packageNumber, numberOfTotalPackages, path, contentType, content
    )
}

class Frame internal constructor(
        method: String = "",
        sessionId: String = "",
        messageId: String = "",
        packageNumber: String = "",
        totalPackages: String = "",
        path: String = "",
        contentType: String = "",
        content: String = "",
        block: ModifyScope.() -> Unit = {}
) {

    private var _method: String = method
        set(value) {
            field = value.escapeBdtpCharacters()
        }
    private var _sessionId: String = sessionId
        set(value) {
            field = value.escapeBdtpCharacters()
        }
    private var _messageId: String = messageId
        set(value) {
            field = value.escapeBdtpCharacters()
        }
    private var _packageNumber: String = packageNumber
        set(value) {
            field = value.escapeBdtpCharacters()
        }
    private var _totalPackages: String = totalPackages
        set(value) {
            field = value.escapeBdtpCharacters()
        }
    private var _path: String = path
        set(value) {
            field = value.escapeBdtpCharacters()
        }
    private var _contentType: String = contentType
        set(value) {
            field = value.escapeBdtpCharacters()
        }
    private var _content: String = content
        set(value) {
            field = value.escapeBdtpCharacters()
        }

    val method get() = _method.unescapeBdtpCharacters()
    val sessionId get() = _sessionId.unescapeBdtpCharacters()
    val messageId get() = _messageId.unescapeBdtpCharacters()
    val packageNumber get() = _packageNumber.unescapeBdtpCharacters()
    val totalPackages get() = _totalPackages.unescapeBdtpCharacters()
    val path get() = _path.unescapeBdtpCharacters()
    val contentType get() = _contentType.unescapeBdtpCharacters()
    val content get() = _content.unescapeBdtpCharacters()

    init {
        modify(block)
    }

    fun copy(): Frame = frame(toString())

    fun modify(block: ModifyScope.() -> Unit): Frame {
        ModifyScope(this).apply(block)
        return this
    }

    override fun toString(): String {
        fun StringBuilder.appendIfNecessary(prefix: Char, value: String) {
            if (value.isBlank()) return

            append("$prefix${value}")
        }
        return buildString {
            appendIfNecessary(Maker.method, _method)
            appendIfNecessary(Maker.sessionId, _sessionId)
            appendIfNecessary(Maker.messageId, _messageId)
            appendIfNecessary(Maker.packageNumber, _packageNumber)
            appendIfNecessary(Maker.totalPackages, _totalPackages)
            appendIfNecessary(Maker.path, _path)
            appendIfNecessary(Maker.contentType, _contentType)
            appendIfNecessary(Maker.content, _content)
        }
    }

    class ModifyScope internal constructor(val frame: Frame) {
        var method
            get() = frame.method
            set(value: String) {
                frame._method = value
            }
        var sessionId
            get() = frame.sessionId
            set(value: String) {
                frame._sessionId = value
            }
        var messageId
            get() = frame.messageId
            set(value: String) {
                frame._messageId = value
            }
        var packageNumber: Long
            get() = frame.packageNumber.toLongOrNull() ?: 1
            set(value: Long) {
                frame._packageNumber = value.toString()
            }
        var totalPackages
            get() = frame.totalPackages.toLongOrNull() ?: 1
            set(value: Long) {
                frame._totalPackages = value.toString()
            }
        var path
            get() = frame.path
            set(value: String) {
                frame._path = value
            }
        var contentType
            get() = frame.contentType
            set(value: String) {
                frame._contentType = value
            }
        var content
            get() = frame.content
            set(value: String) {
                frame._content = value
            }

        fun method(method: Method) {
            this.method = method.toString()
        }

        fun session(session: Session) {
            this.sessionId = session.id
        }

        fun message(message: Message) {
            this.messageId = message.id
            this.contentType = message.contentType
            this.path = message.path
        }

        fun content(text: String) {
            content = text
        }

        fun pack(number: Long, totalPackages: Long) {
            this.packageNumber = number
            this.totalPackages = totalPackages
        }
    }
}

private fun String.replace(oldValue: Char, newValue: String, ignoreCase: Boolean = false) =
        replace(oldValue.toString(), newValue, ignoreCase)

private fun String.replace(oldValue: String, newValue: Char, ignoreCase: Boolean = false) =
        replace(oldValue, newValue.toString(), ignoreCase)


private fun String.escapeBdtpCharacters(): String {
    if (isBlank()) return this

    return replace("\\", "\\\\")
            .replace("°", "\\°")
            .replace(Maker.method, "°m°")
            .replace(Maker.sessionId, "°si°")
            .replace(Maker.messageId, "°mi°")
            .replace(Maker.packageNumber, "°pn°")
            .replace(Maker.totalPackages, "°tp°")
            .replace(Maker.path, "°p°")
            .replace(Maker.contentType, "°ct°")
            .replace(Maker.content, "°c°")
}

private fun String.unescapeBdtpCharacters(): String {
    if (isBlank()) return this

    return replace("°m°", Maker.method)
            .replace("°si°", Maker.sessionId)
            .replace("°mi°", Maker.messageId)
            .replace("°pn°", Maker.packageNumber)
            .replace("°tp°", Maker.totalPackages)
            .replace("°p°", Maker.path)
            .replace("°ct°", Maker.contentType)
            .replace("°c°", Maker.content)
            .replace("\\°", "°")
            .replace("\\\\", "\\")
}

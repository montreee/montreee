package amber.bdtp

import amber.regex.regex

sealed class Method(val code: Int, val name: String) {
    override fun toString() = "$name($code)"

    companion object {

        val shortFormRegex = regex {
            anyOf("p", "c", "e", "s")
            oneOrMore { anyOf('0'..'9') }
        }

        val longFormRegex = regex {
            oneOrMore { letter() }
            literally("(")
            oneOrMore { digit() }
            literally(")")
        }

        fun parse(string: String): Method {
            val methodName: String
            val methodCode: Int
            when {
                longFormRegex.matches(string)  -> {
                    methodName = string.substringBefore("(")
                    methodCode = string.substringAfter("(").substringBefore(")").toInt()
                }
                shortFormRegex.matches(string) -> {
                    methodName = string[0].toString()
                    methodCode = string.substring(1).toInt()
                }
                else                           -> throw Exception("\"$string\" is not a valid method format.")
            }
            return when (methodName) {
                "ping", "p"    -> Ping(methodCode)
                "close", "c"   -> Close(methodCode)
                "error", "e"   -> Error(methodCode)
                "success", "s" -> Success(methodCode)
                else           -> throw Exception("No method with name $methodName.")
            }
        }
    }

    class Ping(code: Int) : Method(code, "ping") {

        companion object {
            fun Ping() = Close(Code.PING)
            fun Pong() = Close(Code.PONG)
        }

        object Code {
            const val PING = 0
            const val PONG = 1
        }

        override fun toString() = "p$code"
    }

    class Close(code: Int) : Method(code, "close") {

        companion object {
            fun Connection() = Close(Code.CLOSE_CONNECTION)
            fun Session() = Close(Code.CLOSE_SESSION)
            fun Message() = Close(Code.CLOSE_MESSAGE)
        }

        object Code {
            const val CLOSE_CONNECTION = 0
            const val CLOSE_SESSION = 1
            const val CLOSE_MESSAGE = 2
        }

        override fun toString() = "c$code"
    }

    class Error(code: Int) : Method(code, "error") {

        companion object {
            fun SyntaxError() = Error(Code.SYNTAX_ERROR)
            fun ConnectionFailed() = Error(Code.CONNECTION_FAILED)
            fun SessionCreationFailed() = Error(Code.SESSION_CREATION_FAILED)
            fun MessageFailed() = Error(Code.MESSAGE_FAILED)
            fun InternalError() = Error(Code.INTERNAL_ERROR)
        }

        object Code {
            const val SYNTAX_ERROR = 0
            const val CONNECTION_FAILED = 1
            const val SESSION_CREATION_FAILED = 2
            const val MESSAGE_FAILED = 3
            const val INTERNAL_ERROR = 4
        }

        override fun toString() = "e$code"
    }

    class Success(code: Int) : Method(code, "success") {

        companion object {
            fun Connected() = Success(Code.CONNECTED)
            fun SessionCreationSuccessfully() = Success(Code.SESSION_CREATED)
            fun MessageReceived() = Success(Code.MESSAGE_RECEIVED)
        }

        object Code {
            const val CONNECTED = 0
            const val SESSION_CREATED = 1
            const val MESSAGE_RECEIVED = 2
        }

        override fun toString() = "s$code"
    }
}

package montreee.parameter.standard

import amber.trial.trial
import montreee.ParsingException
import montreee.parameter.Parser

class ListParser<T>(private val elementParser: Parser<Any?, T>) : Parser<Any?, List<T>>() {

    override fun parse(input: Any?): List<T> {
        val input = trial { input as List<Any?> } alternate { throw ParsingException() }

        elementParser.setup(context, page)

        return input.map {
            elementParser.parse(it)
        }
    }
}

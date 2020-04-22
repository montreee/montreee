package montreee.parameter.standard

import amber.trial.trial
import montreee.ParsingException
import montreee.parameter.Parameter
import montreee.parameter.ParameterLoader
import montreee.parameter.Parser

class StringParameter : Parameter<String>(ParameterLoader(StringParser()))
class StringListParameter : Parameter<List<String>>(ParameterLoader(ListParser(StringParser())))

private class StringParser : Parser<Any?, String>() {
    override fun parse(input: Any?): String {
        return trial { input as String } alternate { throw ParsingException("input must be a String") }
    }
}

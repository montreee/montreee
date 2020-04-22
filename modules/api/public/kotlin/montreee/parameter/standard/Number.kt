package montreee.parameter.standard

import amber.trial.trial
import montreee.ParsingException
import montreee.parameter.Parameter
import montreee.parameter.ParameterLoader
import montreee.parameter.Parser

class NumberParameter : Parameter<Number>(ParameterLoader(NumberParser()))
class NumberListParameter : Parameter<List<Number>>(ParameterLoader(ListParser(NumberParser())))

private class NumberParser : Parser<Any?, Number>() {
    override fun parse(input: Any?): Number {
        return trial { input as Number } alternate { throw ParsingException("input must be a Number") }
    }
}

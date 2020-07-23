package montreee.parameter.standard

import amber.mapquery.query
import amber.trial.trial
import montreee.ParsingException
import montreee.events.module
import montreee.module.Module
import montreee.parameter.Parameter
import montreee.parameter.ParameterLoader
import montreee.parameter.Parser

class ModuleParameter : Parameter<Module>(ParameterLoader(ModuleParser()))
class ModuleListParameter : Parameter<List<Module>>(ParameterLoader(ListParser(ModuleParser())))

private class ModuleParser() : Parser<Any?, Module>() {

    override fun parse(input: Any?): Module {
        val input = trial { input as Map<String, Any?> } alternate { throw ParsingException() }

        val moduleName = input.query()["module"].string
        val moduleLogic = (context.modules[moduleName] ?: throw ParsingException("no module with name $moduleName"))().also {
            context.events.module.create.fire(it)
        }

        val parameterMap = input.query().run {
            if (!contains("parameter")) return@run emptyMap<String, Parameter<*>>()

            trial {
                input.query()["parameter"].map as Map<String, Any?>
            } alternate { throw ParsingException("parameter format it invalid") }
        }

        val parameter = parameterMap.run {
            map {
                val parameterType = it.key.type
                val parameter = (context.parameter[parameterType] ?: throw ParsingException("no parameter with type $parameterType"))()

                parameter.init(context, page, it.value)
                parameter.load()
                Pair(it.key.name, parameter)
            }.toMap()
        }

        return Module(moduleLogic, parameter)
    }
}

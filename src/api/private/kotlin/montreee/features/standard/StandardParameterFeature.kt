package montreee.features.standard

import montreee.dsl.parameter.parameterFactory
import montreee.features.Feature
import montreee.parameter.standard.*

class StandardParameterFeature : Feature() {
    override fun install() {
        context.parameter.apply {
            put(parameterFactory("Module") { ModuleParameter() })
            put(parameterFactory("ModuleList") { ModuleListParameter() })
            put(parameterFactory("Number") { NumberParameter() })
            put(parameterFactory("NumberList") { NumberListParameter() })
            put(parameterFactory("String") { StringParameter() })
            put(parameterFactory("StringList") { StringListParameter() })
        }
    }
}

package montreee.features.standard

import montreee.Engine
import montreee.dsl.feature.featureFactory

suspend fun Engine.installStandardFeatures() {
    install(featureFactory("StandardParameter") { StandardParameterFeature() })
}

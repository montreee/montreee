package montreee.plugin.phases

import montreee.features.FeatureFactory

class FeaturesContext {

    private val features = mutableListOf<FeatureFactory>()
    fun registerFeature(featureFactory: FeatureFactory) {
        features.add(featureFactory)
    }

    val result get() = Result(features)

    data class Result(val value: List<FeatureFactory>)
}

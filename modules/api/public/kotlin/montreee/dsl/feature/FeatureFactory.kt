package montreee.dsl.feature

import montreee.features.Feature
import montreee.features.FeatureFactory

class LambdaFeatureFactory(name: String, val block: () -> Feature) : FeatureFactory(name) {
    override fun invoke() = block()
}

fun featureFactory(name: String, block: () -> Feature) = LambdaFeatureFactory(name, block)

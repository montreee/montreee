package montreee.parameter.standard

internal val String.name: String get() = substringBefore("#")
internal val String.type: String get() = substringAfter("#")

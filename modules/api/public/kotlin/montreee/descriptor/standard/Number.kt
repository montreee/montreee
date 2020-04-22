package montreee.descriptor.standard

import montreee.descriptor.InputType

data class Number(val minValue: Long? = null, val maxValue: Long? = null) : InputType

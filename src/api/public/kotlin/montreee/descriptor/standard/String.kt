package montreee.descriptor.standard

import montreee.descriptor.InputType

data class String(val maxLength: Long = INFINITE_LENGTH) : InputType {
    companion object {
        const val INFINITE_LENGTH = 0L
    }
}

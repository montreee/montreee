package util

const val ALPHANUMERIC_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

fun randomString(length: Int, chars: CharSequence = ALPHANUMERIC_CHARS): String = buildString {
    repeat(length) {
        append(chars.random())
    }
}

fun randomAlphanumericString(length: Int) = randomString(length, ALPHANUMERIC_CHARS)

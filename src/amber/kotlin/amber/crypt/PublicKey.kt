package amber.crypt

interface PublicKey : Key {
    fun encrypt(it: ByteArray): ByteArray
}

package amber.crypt

interface PrivateKey : Key {
    fun decrypt(it: ByteArray): ByteArray
}

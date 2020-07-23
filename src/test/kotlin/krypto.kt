import amber.collections.hexStringToByteArray
import amber.collections.toHex
import amber.crypt.aes.AESKeyPairGenerator
import amber.crypt.rsa.RSAKeyPairGenerator

fun main(args: Array<String>) {
    val message = String(mutableListOf<Byte>().apply { (0 until 128).forEach { add(49) } }.toByteArray())


    val keyPair = RSAKeyPairGenerator(128)()
    println(message)
    val encryptedMessage = keyPair.encrypt(message.toByteArray()).toHex()
    println(encryptedMessage)
    val decryptedMessage = keyPair.decrypt(encryptedMessage.hexStringToByteArray())
    println(String(decryptedMessage))

    println()

    val keyPair2 = AESKeyPairGenerator(128)()
    println(message)
    val encryptedMessage2 = keyPair2.encrypt(message.toByteArray()).toHex()
    println(encryptedMessage2)
    val decryptedMessage2 = keyPair2.decrypt(encryptedMessage2.hexStringToByteArray())
    println(String(decryptedMessage2))
}

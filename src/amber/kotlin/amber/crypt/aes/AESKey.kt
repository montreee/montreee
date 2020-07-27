package amber.crypt.aes

import amber.crypt.Key

interface AESKey : Key {
    val value: ByteArray
}

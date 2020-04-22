package amber.crypt.rsa

import amber.crypt.Key

interface RSAKey : Key {
    val value: ByteArray
}

package com.smokinggunstudio.vezerfonal.network.security

@JsModule("bcryptjs")
@JsNonModule
private external object BcryptJs {
    fun hashSync(data: String, saltOrRounds: Int): String
    fun compareSync(data: String, encrypted: String): Boolean
}

actual object Bcrypt {
    actual fun hashPassword(password: String, saltRounds: Int): String {
        // bcryptjs supports passing the cost directly to hashSync
        return BcryptJs.hashSync(password, saltRounds)
    }
    
    actual fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BcryptJs.compareSync(password, hashedPassword)
    }
}
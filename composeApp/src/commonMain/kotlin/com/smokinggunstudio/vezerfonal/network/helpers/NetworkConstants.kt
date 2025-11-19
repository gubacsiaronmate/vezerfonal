package com.smokinggunstudio.vezerfonal.network.helpers

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse

object NetworkConstants {
    const val BASE_URL = "https://api.vezerfonal.org"
    
    object Endpoints {
        // POST:  ->
        const val REGISTER_DATA_BASIC = "/register/basic" // POST
        /** POST:/register/basic/pfp/{userId}/{rememberMe} -> [TokenResponse] */
        const val REGISTER_PICTURE = "/register/basic/pfp/"
        const val LOGIN_BASIC = "/login/basic" // POST
        const val GET_MESSAGES = "/api/messages/" // GET:/api/messages/{amount}
    }
}
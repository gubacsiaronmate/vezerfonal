package com.smokinggunstudio.vezerfonal.network.helpers

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.data.MessageData
import io.ktor.http.HttpStatusCode

object NetworkConstants {
    const val BASE_URL = "https://api.vezerfonal.org"
    
    object Endpoints {
        /** POST: /register/basic -> success: [HttpStatusCode.Created] + id: [Int] */
        const val REGISTER_DATA_BASIC = "/register/basic"
        /** POST: /register/basic/pfp/{userId}/{rememberMe} -> tokens: [TokenResponse] */
        const val REGISTER_PICTURE = "/register/basic/pfp/"
        /** POST: /login/basic -> tokens: [TokenResponse] */
        const val LOGIN_BASIC = "/login/basic"
        /** GET: /api/messages/{amount} -> messages: [List]<[MessageData]> */
        const val GET_MESSAGES = "/api/messages/"
        /** POST: /api/messages/send */
        const val SEND_MESSAGE = "/api/messages/send"
        /** GET: /api */
        const val AUTH_CHECKER = "/api"
        /** GET: /refresh/{rememberMe} */
        const val REFRESH_REQUEST = "/refresh/"
    }
}
package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.ui.state.LoginState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64

suspend fun loginBasic(loginState: LoginState, client: HttpClient): TokenResponse {
    val encodedCredentials = Base64.encode("${loginState.email}:${loginState.password}".toByteArray())
    
    val response = client.post(NetworkConstants.Endpoints.LOGIN_BASIC) {
         headers {
             append(HttpHeaders.Authorization, "Basic $encodedCredentials")
         }
        setBody(loginState.rememberMe)
    }
    
    return response.body<TokenResponse>()
}
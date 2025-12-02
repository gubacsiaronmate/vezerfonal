package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.Platform
import com.smokinggunstudio.vezerfonal.network.helpers.PlatformType
import com.smokinggunstudio.vezerfonal.ui.state.LoginState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlin.io.encoding.Base64

suspend fun loginBasic(loginState: LoginState, orgExtId: String, client: HttpClient): TokenResponse {
    val encodedCredentials = Base64.encode("${loginState.email}:${loginState.password}".toByteArray())
    
    val rememberMe = when (Platform.type) {
        PlatformType.JS -> false
        PlatformType.Desktop -> false
        else -> loginState.rememberMe
    }
    
    val body = Base64.encode("$rememberMe|$orgExtId".toByteArray())
    
    val response = client.post(NetworkConstants.Endpoints.LOGIN_BASIC) {
        headers {
            append(HttpHeaders.Authorization, "Basic $encodedCredentials")
        }
        
        setBody(body)
    }
    
    return response.body<TokenResponse>()
}
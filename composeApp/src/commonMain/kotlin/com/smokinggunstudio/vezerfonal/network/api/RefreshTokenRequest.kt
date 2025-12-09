package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.request.bearerAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers

suspend fun refreshTokens(refreshToken: String, client: HttpClient): TokenResponse {
    val response = client
        .get(NetworkConstants.Endpoints.REFRESH_REQUEST) {
            bearerAuth(refreshToken)
        }
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnauthorizedException()
    else response.body()
}
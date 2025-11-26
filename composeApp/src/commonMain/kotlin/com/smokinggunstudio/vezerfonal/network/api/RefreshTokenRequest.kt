package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers

suspend fun refreshTokens(refreshToken: String, client: HttpClient): TokenResponse? {
    val response = client.get(NetworkConstants.Endpoints.REFRESH_REQUEST) {
        headers.addTokenAuthHeader(refreshToken)
    }
    return if (response.status != HttpStatusCode.OK) null
    else response.body<TokenResponse>()
}
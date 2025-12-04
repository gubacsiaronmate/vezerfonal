package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.request.bearerAuth
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers

suspend fun logOutRequest(accessToken: String, client: HttpClient): Boolean {
    return try {
        client.get(NetworkConstants.Endpoints.LOGOUT) {
            bearerAuth(accessToken)
        }.status == HttpStatusCode.OK
    } catch (_: Exception) { false }
}
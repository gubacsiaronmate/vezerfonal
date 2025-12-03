package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.*
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode

suspend fun createRegCodeRequest(
    client: HttpClient,
    accessToken: String,
): Boolean {
    return try {
        client.post(NetworkConstants.Endpoints.CREATE_CODE) {
            headers.addTokenAuthHeader(accessToken)
        }.status == HttpStatusCode.OK
    } catch (_: Exception) { false }
}
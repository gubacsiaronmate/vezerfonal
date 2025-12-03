package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.*
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun createRegCode(
    client: HttpClient,
    accessToken: String,
    regCode: RegCodeData,
): Boolean {
    return try {
        client.post(NetworkConstants.Endpoints.CREATE_CODE) {
            headers.addTokenAuthHeader(accessToken); setBody(regCode)
        }.status == HttpStatusCode.OK
    } catch (_: Exception) { false }
}
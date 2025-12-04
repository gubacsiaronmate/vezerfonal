package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun createRegCode(
    client: HttpClient,
    accessToken: String,
    regCode: RegCodeData,
): Boolean {
    return try {
        client.post(NetworkConstants.Endpoints.CREATE_CODE) {
            bearerAuth(accessToken); setBody(regCode)
        }.status == HttpStatusCode.OK
    } catch (_: Exception) { false }
}
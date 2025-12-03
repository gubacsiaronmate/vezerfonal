package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun joinGroupRequest(
    orgExtId: String,
    accessToken: String,
    client: HttpClient
): Boolean = try {
    client.post(NetworkConstants.Endpoints.JOIN_GROUP)
    { headers.addTokenAuthHeader(accessToken); setBody(orgExtId) }
        .status == HttpStatusCode.OK
} catch (_: Exception) { false }
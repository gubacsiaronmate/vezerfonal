package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun joinGroup(
    extId: String,
    accessToken: String,
    client: HttpClient
): GroupData =
    client.post(NetworkConstants.Endpoints.JOIN_GROUP)
    { headers.addTokenAuthHeader(accessToken); setBody(extId) }.body()
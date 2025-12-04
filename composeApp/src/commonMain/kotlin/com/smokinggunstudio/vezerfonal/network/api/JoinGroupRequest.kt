package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.request.bearerAuth
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
    { bearerAuth(accessToken); setBody(extId) }.body()
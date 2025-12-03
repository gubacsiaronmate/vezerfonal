package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun createGroup(
    client: HttpClient,
    accessToken: String,
    groupData: GroupData,
): Boolean = try {
    client.post(NetworkConstants.Endpoints.CREATE_GROUP) {
        headers.addTokenAuthHeader(accessToken); setBody(groupData)
    }.status == HttpStatusCode.OK
} catch (_: Exception) { false }
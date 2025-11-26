package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

suspend fun getGroupData(
    accessToken: String,
    client: HttpClient
): List<GroupData> {
    return client.get(NetworkConstants.Endpoints.GET_GROUP_DATA) {
        headers.addTokenAuthHeader(accessToken)
    }.body<List<GroupData>>()
}
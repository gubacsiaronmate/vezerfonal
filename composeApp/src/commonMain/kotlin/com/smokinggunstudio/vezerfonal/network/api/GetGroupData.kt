package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

suspend fun getGroupData(
    accessToken: String,
    client: HttpClient
): List<GroupData> {
    val response = client.get(NetworkConstants.Endpoints.GET_GROUP_DATA) {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    return response.body<List<GroupData>>()
}
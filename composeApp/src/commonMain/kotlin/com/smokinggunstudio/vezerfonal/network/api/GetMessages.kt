package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.getAccessToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

suspend fun getMessages(amount: Int, client: HttpClient): List<MessageData> {
    val token = getAccessToken(client)
    
    val response = client.get(NetworkConstants.Endpoints.GET_MESSAGES + amount) {
        headers {
            append(HttpHeaders.Authorization, "Bearer $token")
        }
    }
    
    return response.body<List<MessageData>>()
}
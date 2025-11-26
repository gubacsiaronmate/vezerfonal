package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import com.smokinggunstudio.vezerfonal.network.helpers.getAccessToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

suspend fun getMessages(amount: Int, accessToken: String, client: HttpClient): List<MessageData> {
    return client.get(NetworkConstants.Endpoints.GET_MESSAGES + amount) {
        headers.addTokenAuthHeader(accessToken)
    }.body<List<MessageData>>()
}
package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun getMessages(
    amount: Int,
    client: HttpClient,
    accessToken: String,
): List<MessageData> =
    client.get(NetworkConstants.Endpoints.GET_MESSAGES + amount)
    { bearerAuth(accessToken) }.body()
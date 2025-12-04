package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend fun sendMessage(
    message: MessageData,
    accessToken: String,
    client: HttpClient,
) = client.post(NetworkConstants.Endpoints.SEND_MESSAGE)
    { bearerAuth(accessToken); setBody(message) }
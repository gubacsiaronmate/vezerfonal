package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

suspend fun sendMessage(
    message: MessageData,
    accessToken: String,
    client: HttpClient,
): Boolean {
    val response = client
        .post(NetworkConstants.Endpoints.SEND_MESSAGE) {
            bearerAuth(accessToken)
            setBody(message)
        }
    
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnauthorizedException()
    else ok
}
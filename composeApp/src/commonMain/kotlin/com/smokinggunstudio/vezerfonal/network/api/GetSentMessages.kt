package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode

suspend fun getSentMessages(
    amount: Int,
    client: HttpClient,
    accessToken: String,
): List<MessageData> {
    val response = client
        .get(NetworkConstants.Endpoints.GET_SENT_MESSAGES + amount) {
            bearerAuth(accessToken)
        }
    
    val auth = response.status == HttpStatusCode.Unauthorized
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnableToLoadException()
    else if (auth) throw UnauthorizedException()
    else response.body()
}
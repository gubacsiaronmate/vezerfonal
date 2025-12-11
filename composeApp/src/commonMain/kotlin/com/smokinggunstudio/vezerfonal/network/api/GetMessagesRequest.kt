package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode

suspend fun getMessages(
    amount: Int,
    client: HttpClient,
    accessToken: String,
): List<MessageData> {
    val response = client
        .get(NetworkConstants.Endpoints.GET_MESSAGES + amount) {
            bearerAuth(accessToken)
        }
    
    val auth = response.status == HttpStatusCode.Unauthorized
    val ok = response.status == HttpStatusCode.OK
    return if (auth) throw UnauthorizedException()
    else if (!ok) throw UnableToLoadException()
    else response.body()
}
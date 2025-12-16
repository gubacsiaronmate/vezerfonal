package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

suspend fun getReactionsByMessageExtId(
    client: HttpClient,
    accessToken: String,
): List<InteractionInfoData> {
    val url = NetworkConstants.Endpoints.INTERACTIONS + "/reaction/by-message-ext-id"
    val response = client.get(url) { bearerAuth(accessToken) }
    
    val auth = response.status == HttpStatusCode.Unauthorized
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnableToLoadException()
    else if (auth) throw UnauthorizedException()
    else response.body()
}
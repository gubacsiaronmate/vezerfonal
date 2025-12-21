package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserInteractionData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun getReactionsAndUsersByMessageExtId(
    client: HttpClient,
    accessToken: String,
    messageExtId: String,
): List<UserInteractionData> {
    val url = NetworkConstants.Endpoints.INTERACTIONS + "/reaction/by-message-ext-id/$messageExtId"
    val response = client.get(url) { bearerAuth(accessToken) }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> response.body()
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
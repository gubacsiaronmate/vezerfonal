package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.MessageStatusData
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

suspend fun getStatusChangesForMessageByUserExtId(
    client: HttpClient,
    accessToken: String,
    userExtId: ExternalId,
    messageExtId: ExternalId,
): MessageStatusData {
    val url = NetworkConstants.Endpoints
        .GET_STATUS_CHANGES_BY_MESSAGE_EXT_ID + "$messageExtId/by-user-ext-id/$userExtId"
    val response = client.get(url) { bearerAuth(accessToken) }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> response.body()
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
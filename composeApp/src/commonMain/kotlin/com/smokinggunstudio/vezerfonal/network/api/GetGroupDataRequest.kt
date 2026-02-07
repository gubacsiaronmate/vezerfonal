package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun getGroupData(
    accessToken: String,
    client: HttpClient
): List<GroupData> {
    val response = client
        .get(NetworkConstants.Endpoints.GET_GROUP_DATA) {
            bearerAuth(accessToken)
        }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> response.body()
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
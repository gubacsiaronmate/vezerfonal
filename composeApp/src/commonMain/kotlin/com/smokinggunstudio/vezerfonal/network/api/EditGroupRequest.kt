package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun editGroup(
    client: HttpClient,
    accessToken: String,
    groupData: GroupData,
): Boolean {
    val response = client.put(NetworkConstants.Endpoints.EDIT_GROUP) {
        bearerAuth(accessToken)
        setBody(groupData)
    }

    return when (val status = response.status) {
        HttpStatusCode.OK -> true
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

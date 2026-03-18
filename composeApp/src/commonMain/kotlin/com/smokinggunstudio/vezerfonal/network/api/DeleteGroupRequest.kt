package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun deleteGroup(
    client: HttpClient,
    accessToken: String,
    externalId: String,
): Boolean {
    val response = client.delete(NetworkConstants.Endpoints.DELETE_GROUP) {
        bearerAuth(accessToken)
        setBody(externalId)
    }

    return when (val status = response.status) {
        HttpStatusCode.OK -> true
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

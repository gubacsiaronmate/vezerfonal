package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun updateDisplayName(
    client: HttpClient,
    accessToken: String,
    name: String,
) {
    val response = client.patch(NetworkConstants.Endpoints.UPDATE_DISPLAY_NAME) {
        bearerAuth(accessToken)
        setBody(name)
    }

    return when (val status = response.status) {
        HttpStatusCode.OK -> Unit
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

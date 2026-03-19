package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode

suspend fun requestPasswordChangeCode(
    client: HttpClient,
    accessToken: String,
) {
    val response = client.post(NetworkConstants.Endpoints.PASSWORD_CHANGE_REQUEST) {
        bearerAuth(accessToken)
    }

    when (val status = response.status) {
        HttpStatusCode.OK -> return
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

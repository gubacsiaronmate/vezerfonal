package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.ChangePasswordRequest
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun changePassword(
    client: HttpClient,
    accessToken: String,
    request: ChangePasswordRequest,
) {
    val response = client.put(NetworkConstants.Endpoints.PASSWORD_CHANGE) {
        bearerAuth(accessToken)
        setBody(request)
    }

    return when (val status = response.status) {
        HttpStatusCode.OK -> Unit
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        HttpStatusCode.Forbidden -> throw IllegalArgumentException("Érvénytelen kód.")
        else -> throw UnableToLoadException(status)
    }
}

package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.TwoFactorLoginRequest
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

suspend fun twoFactorLogin(
    client: HttpClient,
    request: TwoFactorLoginRequest,
): TokenResponse {
    val response = client.post(NetworkConstants.Endpoints.TWO_FACTOR_LOGIN) {
        contentType(ContentType.Application.Json)
        setBody(request)
    }

    return when (val status = response.status) {
        HttpStatusCode.OK -> response.body()
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        HttpStatusCode.Forbidden -> throw IllegalArgumentException("Érvénytelen kód.")
        else -> throw UnableToLoadException(status)
    }
}

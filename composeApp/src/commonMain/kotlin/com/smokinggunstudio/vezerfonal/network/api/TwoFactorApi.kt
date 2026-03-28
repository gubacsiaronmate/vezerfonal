package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

suspend fun requestTwoFactorCode(client: HttpClient, accessToken: String) {
    val response = client.post(NetworkConstants.Endpoints.TWO_FACTOR_REQUEST) {
        bearerAuth(accessToken)
    }
    when (val status = response.status) {
        HttpStatusCode.OK -> return
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

suspend fun enableTwoFactor(client: HttpClient, accessToken: String, code: Int) {
    val response = client.post(NetworkConstants.Endpoints.TWO_FACTOR_ENABLE) {
        bearerAuth(accessToken)
        contentType(ContentType.Application.Json)
        setBody(code)
    }
    when (val status = response.status) {
        HttpStatusCode.OK -> return
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        HttpStatusCode.Forbidden -> throw IllegalArgumentException("Érvénytelen kód.")
        else -> throw UnableToLoadException(status)
    }
}

suspend fun disableTwoFactor(client: HttpClient, accessToken: String) {
    val response = client.delete(NetworkConstants.Endpoints.TWO_FACTOR_DISABLE) {
        bearerAuth(accessToken)
    }
    when (val status = response.status) {
        HttpStatusCode.OK -> return
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

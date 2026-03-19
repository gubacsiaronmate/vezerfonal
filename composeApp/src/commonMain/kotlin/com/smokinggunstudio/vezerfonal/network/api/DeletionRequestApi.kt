package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

suspend fun approveDeletion(client: HttpClient, accessToken: String, userExternalId: String) {
    val response = client.post(NetworkConstants.Endpoints.APPROVE_DELETION) {
        bearerAuth(accessToken)
        contentType(ContentType.Application.Json)
        setBody(userExternalId)
    }
    when (val status = response.status) {
        HttpStatusCode.OK -> return
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        HttpStatusCode.Forbidden -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

suspend fun denyDeletion(client: HttpClient, accessToken: String, userExternalId: String) {
    val response = client.post(NetworkConstants.Endpoints.DENY_DELETION) {
        bearerAuth(accessToken)
        contentType(ContentType.Application.Json)
        setBody(userExternalId)
    }
    when (val status = response.status) {
        HttpStatusCode.OK -> return
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        HttpStatusCode.Forbidden -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

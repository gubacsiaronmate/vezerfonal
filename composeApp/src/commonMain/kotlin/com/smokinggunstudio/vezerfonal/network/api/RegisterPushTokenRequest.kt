package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

suspend fun registerPushToken(
    accessToken: String,
    client: HttpClient,
    fcmToken: String,
    platform: String,
) {
    val url = ""
    val response = client.post(url) {
        bearerAuth(accessToken)
        setBody(mapOf(
            "token" to fcmToken,
            "platform" to platform
        ))
        contentType(ContentType.Application.Json)
    }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> Unit
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
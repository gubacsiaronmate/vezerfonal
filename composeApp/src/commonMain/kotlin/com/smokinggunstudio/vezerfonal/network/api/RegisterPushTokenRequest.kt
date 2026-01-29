package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.PushToken
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
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
    val url = NetworkConstants.Endpoints.NEW_PUSH_TOKEN
    
    val response = client.post(url) {
        bearerAuth(accessToken)
        setBody(PushToken(
            token = fcmToken,
            platform = platform
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
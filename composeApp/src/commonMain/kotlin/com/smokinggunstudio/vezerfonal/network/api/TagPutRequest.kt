package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun tagPut(
    client: HttpClient,
    accessToken: String,
    tag: TagData
) {
    val url = NetworkConstants.Endpoints.TAG_UPDATE
    val response = client.put(url) {
        bearerAuth(accessToken)
        setBody(tag)
    }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> Unit
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
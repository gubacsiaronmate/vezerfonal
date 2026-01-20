package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun tagCreate(
    client: HttpClient,
    accessToken: String,
    tag: TagData
) {
    val url = NetworkConstants.Endpoints.TAG_CREATE
    val response = client.post(url) {
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
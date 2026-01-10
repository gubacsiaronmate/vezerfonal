package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

@Throws(UnauthorizedException::class, UnableToLoadException::class)
suspend fun deleteRegCode(
    code: String,
    client: HttpClient,
    accessToken: String,
) {
    val url = NetworkConstants.Endpoints.DELETE_REG_CODE
    val response = client.delete(url) {
        bearerAuth(accessToken)
        setBody(code)
    }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> Unit
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
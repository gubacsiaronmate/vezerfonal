package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun createRegCode(
    client: HttpClient,
    accessToken: String,
    regCode: RegCodeData,
): Boolean {
    val response = client
        .post(NetworkConstants.Endpoints.CREATE_CODE) {
            bearerAuth(accessToken)
            setBody(regCode)
        }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> true
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun patchCode(
    client: HttpClient,
    accessToken: String,
    regCode: RegCodeData,
) {
    val url = NetworkConstants.Endpoints.PATCH_REG_CODE
    val response = client.patch(url) {
        bearerAuth(accessToken)
        setBody(regCode)
    }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> Unit
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
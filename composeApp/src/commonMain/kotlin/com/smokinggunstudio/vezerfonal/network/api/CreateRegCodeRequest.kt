package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
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
    
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnauthorizedException()
    else ok
}
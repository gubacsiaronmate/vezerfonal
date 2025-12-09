package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.request.bearerAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun getUsersByIdentifierList(
    identifiers: List<String>,
    accessToken: String,
    client: HttpClient
): List<UserData> {
    val response = client
        .post(NetworkConstants.Endpoints.GET_USERS_BY_IDENTIFIER_LIST) {
            bearerAuth(accessToken); setBody(identifiers)
        }
    
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnauthorizedException()
    else response.body()
}
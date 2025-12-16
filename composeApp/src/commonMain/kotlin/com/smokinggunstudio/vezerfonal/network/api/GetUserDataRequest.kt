package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.request.bearerAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

suspend fun getUserData(accessToken: String, client: HttpClient): UserData {
    val response = client
        .get(NetworkConstants.Endpoints.GET_USER_DATA) {
            bearerAuth(accessToken)
        }
    
    val asd = response.body<UserData>()
    log {"Got ${asd.name}"}
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnauthorizedException()
    else asd
}
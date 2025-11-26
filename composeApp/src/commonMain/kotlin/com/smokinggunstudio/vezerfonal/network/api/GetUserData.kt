package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.getAccessToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

suspend fun getUserData(accessToken: String, client: HttpClient): UserData {
    val response = client.get(NetworkConstants.Endpoints.GET_USER_DATA) {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    
    return response.body<UserData>()
}
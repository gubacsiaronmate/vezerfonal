package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.request.bearerAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun getAllUsers(
    accessToken: String,
    client: HttpClient
): List<UserData> =
    client.get(NetworkConstants.Endpoints.GET_ALL_USERS) {
        bearerAuth(accessToken)
    }.body()
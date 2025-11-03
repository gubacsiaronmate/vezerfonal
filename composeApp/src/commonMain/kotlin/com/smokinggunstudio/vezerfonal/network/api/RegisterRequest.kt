package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend fun register(userData: UserData, client: HttpClient) {
    val response = client.post(NetworkConstants.Endpoints.REGISTER_DATA_BASIC) {
        setBody(userData)
    }
    // TODO: implement token generation in the backend and return a token
}
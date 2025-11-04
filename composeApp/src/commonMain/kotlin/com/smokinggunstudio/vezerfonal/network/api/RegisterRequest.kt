package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun registerBasic(userData: UserData, client: HttpClient) {
    val response = client.post(NetworkConstants.Endpoints.REGISTER_DATA_BASIC) {
        setBody(userData)
    }
    if (response.status != HttpStatusCode.Created) error("Could not register.")
    
}
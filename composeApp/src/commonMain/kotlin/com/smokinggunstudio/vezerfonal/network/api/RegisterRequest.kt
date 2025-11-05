package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun registerBasic(userData: UserData, pfp: FileData, client: HttpClient): TokenResponse {
    val success = (client.post(
        NetworkConstants.Endpoints.REGISTER_DATA_BASIC
    ) { setBody(userData) }).status == HttpStatusCode.Created
    if (!success) error("Could not register.")
    val response = client.post(
        NetworkConstants.Endpoints.REGISTER_PICTURE
    ) { setBody(pfp) }
    
    val tokens = response.body<TokenResponse>()
    return tokens
}
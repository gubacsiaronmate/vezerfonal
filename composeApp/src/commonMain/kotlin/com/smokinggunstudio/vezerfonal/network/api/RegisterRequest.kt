package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.Platform
import com.smokinggunstudio.vezerfonal.network.helpers.PlatformType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun registerBasic(
    userData: UserData,
    rememberMe: Boolean,
    fileData: FileData,
    client: HttpClient
): TokenResponse {
    val (id: Int, success: Boolean) = (
        client.post(
            NetworkConstants.Endpoints.REGISTER_DATA_BASIC
        ) { setBody(userData) }
    ).let {
        Pair(
            it.body<String>().trim().toInt(),
            it.status == HttpStatusCode.Created
        )
    }
    
    if (!success) error("Could not register.")
    
    val urlMetadata = NetworkConstants.Endpoints.REGISTER_PICTURE + id +
        when (Platform.type) {
            PlatformType.Desktop -> "/false"
            PlatformType.JS -> "/false"
            else -> "/$rememberMe"
        } + "/metadata"
    
    val acceptance = (client.post(urlMetadata) { setBody(fileData.metaData) }).status == HttpStatusCode.Accepted
    
    if (!acceptance) error("Metadata didn't get received.")
    
    val urlData = NetworkConstants.Endpoints.REGISTER_PICTURE + id +
            when (Platform.type) {
                PlatformType.Desktop -> "/false"
                PlatformType.JS -> "/false"
                else -> "/$rememberMe"
            } + "/filedata"
    
    val response = client.post(urlData) { setBody(fileData.bytes) }
    
    val tokens = response.body<TokenResponse>()
    return tokens
}
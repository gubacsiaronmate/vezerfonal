package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.request.bearerAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun getAllTags(
    accessToken: String,
    client: HttpClient
): List<TagData> = client.get(NetworkConstants.Endpoints.GET_ALL_TAGS){
    bearerAuth(accessToken)
}.body()
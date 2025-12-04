package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend fun getUsersByIdentifierList(
    identifiers: List<String>,
    accessToken: String,
    client: HttpClient
): List<UserData> = client.post(NetworkConstants.Endpoints.GET_USERS_BY_IDENTIFIER_LIST) {
    headers.addTokenAuthHeader(accessToken); setBody(identifiers)
}.body()
package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun getAllRegCodes(
    accessToken: String,
    client: HttpClient,
): List<RegCodeData> =
    client.get(NetworkConstants.Endpoints.GET_ALL_REG_CODES)
    { headers.addTokenAuthHeader(accessToken) }.body()
package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun getAllRegCodes(
    accessToken: String,
    client: HttpClient,
): List<RegCodeData> =
    client.get(NetworkConstants.Endpoints.GET_ALL_REG_CODES)
    { bearerAuth(accessToken) }.body()
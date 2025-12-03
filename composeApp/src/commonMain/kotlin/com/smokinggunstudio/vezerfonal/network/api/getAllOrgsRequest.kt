package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun getAllOrgsRequest(client: HttpClient): List<OrgData> =
    client.get(NetworkConstants.Endpoints.GET_ORGS).body()
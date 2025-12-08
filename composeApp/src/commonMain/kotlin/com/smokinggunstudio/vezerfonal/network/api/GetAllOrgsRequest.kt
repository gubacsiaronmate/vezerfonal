package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

suspend fun getAllOrgsRequest(client: HttpClient): List<OrgData> {
    val response = try {
        client.get(NetworkConstants.Endpoints.GET_ORGS)
    } catch (e: Exception) { throw e }
    
    return response.body()
}
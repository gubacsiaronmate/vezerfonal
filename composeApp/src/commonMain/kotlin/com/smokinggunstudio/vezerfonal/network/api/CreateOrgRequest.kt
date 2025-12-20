package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.helpers.NotCreatedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun createOrgRequest(
    org: OrgData,
    client: HttpClient
): Boolean {
    val response = client
        .post(NetworkConstants.Endpoints.CREATE_ORG) { setBody(org) }
    
    when (response.status) {
        HttpStatusCode.OK -> return true
        else -> throw NotCreatedException()
    }
}
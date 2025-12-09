package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun createGroup(
    client: HttpClient,
    accessToken: String,
    groupData: GroupData,
): Boolean {
    val response = client
        .post(NetworkConstants.Endpoints.CREATE_GROUP) {
            bearerAuth(accessToken); setBody(groupData)
        }
    
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnauthorizedException()
    else ok
}
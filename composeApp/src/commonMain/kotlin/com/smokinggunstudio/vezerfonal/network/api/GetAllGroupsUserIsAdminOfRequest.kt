package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.UnauthorizedException
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode

suspend fun getAllGroupsUserIsAdminOf(
    accessToken: String,
    client: HttpClient
): List<GroupData> {
    val response = client
        .get(NetworkConstants.Endpoints.GET_ALL_GROUPS_USER_IS_ADMIN_OF) {
            bearerAuth(accessToken)
        }
    
    val ok = response.status == HttpStatusCode.OK
    return if (!ok) throw UnauthorizedException()
    else response.body()
}
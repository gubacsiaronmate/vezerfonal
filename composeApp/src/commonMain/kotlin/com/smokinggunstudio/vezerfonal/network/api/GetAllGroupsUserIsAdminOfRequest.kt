package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.addTokenAuthHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

suspend fun getAllGroupsUserIsAdminOf(
    accessToken: String,
    client: HttpClient
): List<GroupData> =
    client.get(NetworkConstants.Endpoints.GET_ALL_GROUPS_USER_IS_ADMIN_OF) {
        headers.addTokenAuthHeader(accessToken)
    }.body()
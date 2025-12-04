package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * @param accessToken used to request access and authorize
 * @param client [HttpClient] used to send the request
 *
 * @return `true` if authorized otherwise `false`
 * */
suspend fun checkAccessTokenValidity(accessToken: String, client: HttpClient): Boolean {
    return try {
        client.get(NetworkConstants.Endpoints.AUTH_CHECKER) {
            bearerAuth(accessToken)
        }.status == HttpStatusCode.OK
    } catch (_: Exception) { false }
}
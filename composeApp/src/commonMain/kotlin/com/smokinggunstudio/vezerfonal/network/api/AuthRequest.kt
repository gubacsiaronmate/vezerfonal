package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

/**
 * @param accessToken used to request access and authorize
 * @param client [HttpClient] used to send the request
 *
 * @return `true` if authorized otherwise `false`
 * */
suspend fun checkAccessTokenValidity(accessToken: String, client: HttpClient): Boolean {
    return try {
        client.get(NetworkConstants.Endpoints.AUTH_CHECKER) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }.status == HttpStatusCode.OK
    } catch (_: Exception) { false }
}
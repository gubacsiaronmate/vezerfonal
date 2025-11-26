package com.smokinggunstudio.vezerfonal.network.helpers

import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders

fun HeadersBuilder.addTokenAuthHeader(token: String) = append(HttpHeaders.Authorization, "Bearer $token")
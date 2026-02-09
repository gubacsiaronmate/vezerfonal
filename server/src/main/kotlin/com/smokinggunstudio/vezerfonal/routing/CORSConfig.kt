package com.smokinggunstudio.vezerfonal.routing

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.plugins.cors.CORSConfig

fun CORSConfig.initialize() {
    allowHost("vezerfonal.org")
    allowHost("localhost:8080")
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowHeader(HttpHeaders.Authorization)
    allowHeader(HttpHeaders.ContentType)
}
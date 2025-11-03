package com.smokinggunstudio.vezerfonal.network.client

import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient = HttpClient(provideEngine()) { installPlugins() }

expect fun provideEngine(): HttpClientEngine

private fun HttpClientConfig<*>.installPlugins() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = false
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30000
    }
    defaultRequest {
        url(NetworkConstants.BASE_URL)
        contentType(ContentType.Application.Json)
    }
}
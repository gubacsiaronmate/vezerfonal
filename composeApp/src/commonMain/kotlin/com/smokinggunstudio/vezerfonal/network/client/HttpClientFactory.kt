package com.smokinggunstudio.vezerfonal.network.client

import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(baseUrl: Boolean = true): HttpClient = HttpClient(provideEngine()) { installPlugins(baseUrl) }

expect fun provideEngine(): HttpClientEngine

private fun HttpClientConfig<*>.installPlugins(baseUrl: Boolean) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = false
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
    }
    defaultRequest {
        if (baseUrl) url(NetworkConstants.BASE_URL)
        contentType(ContentType.Application.Json)
    }
}
package com.smokinggunstudio.vezerfonal.router

import com.smokinggunstudio.vezerfonal.helpers.ImageService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.routing

fun Application.configureRouting(imageService: ImageService) {
    install(ContentNegotiation) {
        json()
    }
    
    routing {
    
    }
}
package com.smokinggunstudio.vezerfonal.router

import com.smokinggunstudio.vezerfonal.helpers.ImageService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlin.coroutines.CoroutineContext

suspend fun Application.configureRouting(imageService: ImageService, context: CoroutineContext) {
    install(ContentNegotiation) {
        json()
    }
    
    routing {
        route("/home") {
            get {
            
            }
            post {
            
            }
        }
    }
}
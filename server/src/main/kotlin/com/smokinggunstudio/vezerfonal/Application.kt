package com.smokinggunstudio.vezerfonal

import com.smokinggunstudio.vezerfonal.database.MainDB
import com.smokinggunstudio.vezerfonal.database.configureDatabase
import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.routing.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() = runBlocking {
    val url: String =
        environment.config
            .propertyOrNull("ktor.database.url")?.getString()
        ?: error("Ktor database not initialized")
    
    val username: String =
        environment.config
            .propertyOrNull("ktor.database.username")?.getString()
        ?: error("Ktor database not initialized")
    
    val password: String =
        environment.config
            .propertyOrNull("ktor.database.password")?.getString()
        ?: error("Ktor database not initialized")
    
    val pfpDirectories =
        environment.config.propertyOrNull("ktor.application.directories.pfps")?.getString()
        ?: throw NoSuchFieldException("Unable to get path to pictures directory: No such environment variable.")
    
    val imageService = ImageService(pfpDirectories)
    
    configureDatabase(url, username, password)
    configureRouting(imageService, MainDB!!)
}
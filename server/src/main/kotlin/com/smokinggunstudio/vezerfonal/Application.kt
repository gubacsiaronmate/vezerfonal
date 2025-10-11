package com.smokinggunstudio.vezerfonal

import com.smokinggunstudio.vezerfonal.database.configureDatabase
import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.router.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() = runBlocking {
    val url: String? = environment.config.propertyOrNull("ktor.database.url")?.getString()
    val username: String? = environment.config.propertyOrNull("ktor.database.username")?.getString()
    val password: String? = environment.config.propertyOrNull("ktor.database.password")?.getString()
    val pfpDirectories = environment.config.propertyOrNull("ktor.application.directories.pfps")?.getString()
    val context = Dispatchers.IO
    val imageService = if (pfpDirectories != null) ImageService(pfpDirectories)
    else throw NoSuchFieldException("Unable to get path to pictures directory: No such environment variable.")
    
    if (url != null && username != null && password != null)
        async(context) { configureDatabase(url, username, password, context) }.await()
    configureRouting(imageService)
}
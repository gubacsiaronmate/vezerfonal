package com.smokinggunstudio.vezerfonal

import com.smokinggunstudio.vezerfonal.database.configureDatabase
import com.smokinggunstudio.vezerfonal.database.ensureOrgDb
import com.smokinggunstudio.vezerfonal.database.mainDb
import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.objects.Organisations
import com.smokinggunstudio.vezerfonal.routing.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() = runBlocking {
//    val url: String? = environment.config.propertyOrNull("ktor.database.url")?.getString()
    val urlBase: String? = environment.config.propertyOrNull("ktor.database.urlBase")?.getString()
    val username: String? = environment.config.propertyOrNull("ktor.database.username")?.getString()
    val password: String? = environment.config.propertyOrNull("ktor.database.password")?.getString()
    val pfpDirectories = environment.config.propertyOrNull("ktor.application.directories.pfps")?.getString()
    val context = Dispatchers.IO
    val imageService = if (pfpDirectories != null) ImageService(pfpDirectories)
    else throw NoSuchFieldException("Unable to get path to pictures directory: No such environment variable.")
    
    if (urlBase != null && username != null && password != null)
        async(context) { configureDatabase(urlBase, username, password, context) }.await()
    configureRouting(imageService, context)
    
    transaction(mainDb) {
        Organisations.insert {
            it[name] = "testorg"
        }
    }
    
    val orgDb = async(context) {
        ensureOrgDb("testorg", context)
    }.await()
    if (orgDb == null) {
        System.err.println("orgDb is null, not gud")
    }
    
    println("orgDb got")
}
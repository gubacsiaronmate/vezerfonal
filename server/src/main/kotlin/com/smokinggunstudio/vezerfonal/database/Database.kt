package com.smokinggunstudio.vezerfonal.database

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import kotlin.system.exitProcess

suspend fun configureDatabase(url: String, username: String, password: String, context: CoroutineDispatcher) = withContext(context) {
    try {
        Database.connect(
            driver = "org.postgresql.Driver",
            url = url,
            user = username,
            password = password
        )
    } catch (e: IllegalStateException) {
        System.err.println("Unable to connect to database at url: $url\nError: ${e.message}")
        exitProcess(1)
    }
}
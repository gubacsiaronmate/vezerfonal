package com.smokinggunstudio.vezerfonal.database

import com.smokinggunstudio.vezerfonal.helpers.makeArrayOfTable
import com.smokinggunstudio.vezerfonal.objects.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import kotlin.system.exitProcess

suspend fun configureDatabase(url: String, username: String, password: String, context: CoroutineDispatcher) = withContext(context) {
    try {
        Database.connect(
            driver = "org.postgresql.Driver",
            url = url,
            user = username,
            password = password
        )
        
//        MigrationUtils.statementsRequiredForDatabaseMigration(*makeArrayOfTable(
//            Groups, Organisations, JWTs, Users, RegistrationCodes
//        ))
    } catch (e: IllegalStateException) {
        System.err.println("Unable to connect to database at url: $url\nError: ${e.message}")
        exitProcess(1)
    }
}
package com.smokinggunstudio.vezerfonal.database

import com.smokinggunstudio.vezerfonal.helpers.makeArrayOfTable
import com.smokinggunstudio.vezerfonal.objects.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import kotlin.system.exitProcess

val mainTables = makeArrayOfTable(
    RegistrationCodes, Organisations
)

val orgTables = makeArrayOfTable(
    Groups,
    JWTs,
    Messages,
    MessageTag,
    MessageTagConnection,
    MessageUserInteractions,
    UserGroupConnection,
    UserNotificationSettings,
    UserOAuthProvider,
    Users
)

suspend fun configureDatabase(url: String, username: String, password: String, context: CoroutineDispatcher) =
    withContext(context) {
        try {
            val db = Database.connect(
                driver = "org.postgresql.Driver", url = url, user = username, password = password
            )
            
            transaction(db) {
                val statements = MigrationUtils.statementsRequiredForDatabaseMigration(
                    *orgTables
                )
                
                statements.forEach {
                    exec(it)
                }
            }
            
            return@withContext db
        } catch (e: IllegalStateException) {
            System.err.println("Unable to connect to database at url: $url\nError: ${e.message}")
            exitProcess(1)
        }
    }
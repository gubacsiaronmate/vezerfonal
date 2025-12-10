package com.smokinggunstudio.vezerfonal.database

import com.smokinggunstudio.vezerfonal.helpers.getExtId
import com.smokinggunstudio.vezerfonal.helpers.makeArrayOfTable
import com.smokinggunstudio.vezerfonal.objects.*
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.core.Schema
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess
import kotlin.text.filter

private val mainTables = makeArrayOfTable(
    RegistrationCodes, Organisations
)

private val orgTables = makeArrayOfTable(
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

var MainDB: Database? = null
val OrgDBs: MutableMap<String, Database> = mutableMapOf()

private var dbUrl: String = ""
private var dbUsername: String = ""
private var dbPassword: String = ""

private fun connect(schema: String? = null): Database = Database.connect(
    driver = "org.postgresql.Driver",
    url = dbUrl,
    user = dbUsername,
    password = dbPassword,
    databaseConfig = DatabaseConfig {
        defaultMaxAttempts = 3
        defaultSchema = schema?.let { Schema(it) }
    }
)

suspend fun configureDatabase(url: String, username: String, password: String, context: CoroutineContext) =
    withContext(context) {
        try {
            dbUrl = url
            dbUsername = username
            dbPassword = password
            
            val db = connect("vezerfonal_main")
            
            transaction(db) {
                exec("SHOW search_path;") { rs ->
                    if (rs.next())
                        if (rs.getString(1) != "vezerfonal_main")
                            error("I give up")
                }
                val statements = MigrationUtils.statementsRequiredForDatabaseMigration(*mainTables)
                
                statements.forEach(::exec)
            }
            
            MainDB = db
        } catch (e: Exception) {
            System.err.println("Unable to connect to database at url: $url\nError: ${e.message}")
            exitProcess(1)
        }
    }

private class NoDBGotWithSchemaNameException : Exception()

suspend fun ensureOrgDB(name: String, context: CoroutineContext): Database? =
    withContext(context) {
        val escapedName = name.filter { it.isLetter() }
        val schemaName = "vezerfonal_org_$escapedName"
        
        if (OrgDBs.containsKey(escapedName)) OrgDBs[escapedName]
        
        try {
            val db = connect(schemaName)
            
            transaction(db) {
                exec("CREATE SCHEMA IF NOT EXISTS $schemaName;")
                
                exec(
                    """SELECT 1
                        |FROM pg_type t
                        |JOIN pg_namespace n ON n.oid = t.typnamespace
                        |WHERE t.typname = 'interaction_type' AND n.nspname = current_schema()""".trimMargin()
                ) {
                    if (!it.next())
                        exec(
                            """create type interaction_type as
                                |enum ('status', 'reaction', 'nudge', 'archive')""".trimMargin()
                        )
                }
                
                exec(
                    """SELECT 1
                        |FROM pg_type t
                        |JOIN pg_namespace n ON n.oid = t.typnamespace
                        |WHERE t.typname = 'message_status' AND n.nspname = current_schema()""".trimMargin()
                ) {
                    if (!it.next())
                        exec(
                            """create type message_status as
                                |enum ('sent', 'received', 'read');""".trimMargin()
                        )
                }
                
                val statements = MigrationUtils.statementsRequiredForDatabaseMigration(*orgTables)
                
                statements.forEach(::exec)
            }
            
            OrgDBs[escapedName] = db
            
            return@withContext db
        } catch (e: Exception) {
            System.err.println("Unable to connect to org database at url: $dbUrl?currentSchema=$schemaName\nError: ${e.message}")
            return@withContext null
        }
    }
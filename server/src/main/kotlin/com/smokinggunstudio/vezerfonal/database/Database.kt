package com.smokinggunstudio.vezerfonal.database

import com.smokinggunstudio.vezerfonal.helpers.makeArrayOfTable
import com.smokinggunstudio.vezerfonal.objects.*
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
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

private var mainDbUrlBase: String = ""
private var mainDbUsername: String = ""
private var mainDbPassword: String = ""

suspend fun configureDatabase(urlBase: String, username: String, password: String, context: CoroutineContext) =
    withContext(context) {
        val url = urlBase + "vezerfonal_main"
        
        try {
            val db = Database.connect(
                driver = "org.postgresql.Driver",
                url = url,
                user = username,
                password = password,
                databaseConfig = DatabaseConfig { defaultMaxAttempts = 3 }
            )
            
            mainDbUrlBase = urlBase
            mainDbUsername = username
            mainDbPassword = password
            
            transaction(db) {
                exec(stmt = "SET search_path = vezerfonal_main;")
                val statements = MigrationUtils.statementsRequiredForDatabaseMigration(*mainTables)
                
                statements.forEach(::exec)
            }
            
            MainDB = db
        } catch (e: Exception) {
            System.err.println("Unable to connect to database at url: $url\nError: ${e.message}")
            exitProcess(1)
        }
    }

suspend fun ensureOrgDb(name: String, context: CoroutineContext): Database? =
    withContext(context) {
        val escapedName = name.filter { it.isLetter() }
        val schemaName = "vezerfonal_org_$escapedName"
        val url = mainDbUrlBase + schemaName
        
        if (OrgDBs.containsKey(escapedName)) OrgDBs[escapedName]
        
        try {
            val db = Database.connect(
                driver = "org.postgresql.Driver",
                url = url,
                user = mainDbUsername,
                password = mainDbPassword,
                databaseConfig = DatabaseConfig { defaultMaxAttempts = 3 },
            )
            
            transaction(db) {
                exec(stmt = "CREATE SCHEMA IF NOT EXISTS $schemaName;")
                exec(stmt = "SET search_path = $schemaName;")
                
                exec(
                    """SELECT 1
                        |FROM pg_type t
                        |JOIN pg_namespace n ON n.oid = t.typnamespace
                        |WHERE t.typname = 'interaction_type' AND n.nspname = current_schema()""".trimMargin()
                ) {
                    if (!it.next())
                        exec(
                            """create type interaction_type as
                                |enum ('status', 'reaction', 'mention', 'nudge', 'archive')""".trimMargin()
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
                
                val statements = MigrationUtils.statementsRequiredForDatabaseMigration(
                    *orgTables
                )
                
                statements.forEach(::exec)
            }
            
            OrgDBs[escapedName] = db
            
            return@withContext db
        } catch (e: Exception) {
            System.err.println("Unable to connect to org database at url: $url\nError: ${e.message}")
            return@withContext null
        }
    }
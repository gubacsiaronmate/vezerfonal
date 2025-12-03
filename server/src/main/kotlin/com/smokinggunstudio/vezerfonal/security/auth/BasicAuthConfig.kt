package com.smokinggunstudio.vezerfonal.security.auth

import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import com.smokinggunstudio.vezerfonal.security.verifyPassword
import io.ktor.server.auth.*
import io.ktor.server.request.*
import kotlinx.io.bytestring.decode
import kotlinx.io.bytestring.decodeToByteString
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.coroutines.CoroutineContext
import kotlin.io.encoding.Base64

fun configureBasicAuth(feature: AuthenticationConfig, mainDB: Database, context: CoroutineContext) {
    feature.basic("basic") {
        validate { credentials ->
            val data = Base64.decodeToByteString(receive<String>()).toByteArray().decodeToString()
            val (rememberMe, externalId) =
                data.let { Pair(it.substringBefore("|").toBoolean(), it.substringAfterLast("|")) }
            
            val org = OrganisationRepository(mainDB).getOrganisationByExternalId(externalId)
                ?: error("Cannot resolve org by extId: $externalId")
            
            val db = ensureOrgDB(org.name, context)
                ?: error("")
            
            val user = UserRepository(db).getUserByEmail(credentials.name)
                ?: return@validate null
            
            val isPasswordValid = verifyPassword(credentials.password, user.password)
            
            if (isPasswordValid)
                AuthResponse(user, db, org, rememberMe)
            else null
        }
    }
}
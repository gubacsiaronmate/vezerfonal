package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Organisation
import com.smokinggunstudio.vezerfonal.objects.Organisations
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getOrganisation(context: CoroutineContext): Organisation = withContext(context) {
    return@withContext transaction { 
        val org = Organisations.selectAll().first()
        return@transaction Organisation(
            name = org[Organisations.name],
            createdAt = org[Organisations.createdAt]
        )
    }
}
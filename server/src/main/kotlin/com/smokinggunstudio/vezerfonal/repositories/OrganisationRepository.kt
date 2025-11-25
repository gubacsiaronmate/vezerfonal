package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Organisation
import com.smokinggunstudio.vezerfonal.objects.Organisations
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getOrganisation(): Organisation =
    newSuspendedTransaction {
        val org = Organisations.selectAll().toList().single()
        Organisation(
            name = org[Organisations.name],
            createdAt = org[Organisations.createdAt]
        )
    }
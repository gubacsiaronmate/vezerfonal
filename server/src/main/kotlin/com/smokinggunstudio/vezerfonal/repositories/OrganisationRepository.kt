package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Organisation
import com.smokinggunstudio.vezerfonal.objects.Organisations
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun getOrganisation(): Organisation =
    suspendTransaction {
        val org = Organisations.selectAll().toList().single()
        Organisation(
            name = org[Organisations.name],
            createdAt = org[Organisations.createdAt]
        )
    }
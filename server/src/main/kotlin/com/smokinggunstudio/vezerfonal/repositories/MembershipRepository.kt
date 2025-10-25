package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import kotlin.coroutines.CoroutineContext

suspend fun getAllMemberShips(
    context: CoroutineContext
): List<ResultRow> = withContext(context)
{ UserGroupConnection.selectAll().toList() }
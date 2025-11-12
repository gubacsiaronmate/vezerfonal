package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.RegistrationCode
import com.smokinggunstudio.vezerfonal.objects.RegistrationCodes
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllCodes(context: CoroutineContext): List<RegistrationCode> = withContext(context) {
    return@withContext transaction {
        val code = RegistrationCodes.selectAll()
        return@transaction code.map {
            RegistrationCode(
                id = it[RegistrationCodes.id],
                code = it[RegistrationCodes.code],
                totalUses = it[RegistrationCodes.totalUses],
                remainingUses = it[RegistrationCodes.remainingUses]
            )
        }
    }
}

suspend fun getCodeByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): RegistrationCode? = withContext(context) {
    RegistrationCodes.select(condition).firstOrNull()?.let {
        RegistrationCode(
            id = it[RegistrationCodes.id],
            code = it[RegistrationCodes.code],
            totalUses = it[RegistrationCodes.totalUses],
            remainingUses = it[RegistrationCodes.remainingUses]
        )
    }
}

suspend fun getCodesByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): List<RegistrationCode> = withContext(context) {
    RegistrationCodes.select(condition).map {
        RegistrationCode(
            id = it[RegistrationCodes.id],
            code = it[RegistrationCodes.code],
            totalUses = it[RegistrationCodes.totalUses],
            remainingUses = it[RegistrationCodes.remainingUses]
        )
    }
}

suspend fun getCodeById(
    id: Int,
    context: CoroutineContext
): RegistrationCode? = newSuspendedTransaction { getCodeByCondition(context) { RegistrationCodes.id eq id } }

suspend fun getCodeByCode(
    code: String,
    context: CoroutineContext
): RegistrationCode? = newSuspendedTransaction { getCodeByCondition(context) { RegistrationCodes.code eq code } }

suspend fun insertCode(
    registrationCode: RegistrationCode,
    context: CoroutineContext
): Boolean = withContext(context) {
    return@withContext if (getAllCodes(context).none { code ->
        code.id == registrationCode.id
    }) transaction {
        RegistrationCodes.insert {
            it[code] = registrationCode.code
            it[totalUses] = registrationCode.totalUses
            it[remainingUses] = registrationCode.remainingUses
        }.insertedCount == 1
    } else false
}

suspend fun insertCodes(
    registrationCodes: List<RegistrationCode>,
    context: CoroutineContext
): List<Boolean> = registrationCodes.map { insertCode(it, context) }

suspend fun doesCodeExist(
    code: String,
    context: CoroutineContext
): Boolean = getCodeByCode(code, context) != null
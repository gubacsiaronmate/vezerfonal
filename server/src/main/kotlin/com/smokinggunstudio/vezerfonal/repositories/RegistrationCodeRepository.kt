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
import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import org.jetbrains.exposed.sql.ResultRow

private fun ResultRow.toCode(): RegistrationCode = RegistrationCode(
    id = this[RegistrationCodes.id],
    code = this[RegistrationCodes.code],
    totalUses = this[RegistrationCodes.totalUses],
    remainingUses = this[RegistrationCodes.remainingUses]
)

suspend fun getAllCodes(): List<RegistrationCode> =
    newSuspendedTransaction {
        RegistrationCodes.selectAll().map { it.toCode() }
    }

suspend fun getCodeByCondition(
    condition: SQLCondition
): RegistrationCode? = newSuspendedTransaction {
    RegistrationCodes.select(condition).toList().ifNotEmpty()?.single()?.toCode()
}

suspend fun getCodesByCondition(
    condition: SQLCondition
): List<RegistrationCode> = newSuspendedTransaction {
    RegistrationCodes.select(condition).map { it.toCode() }
}

suspend fun getCodeById(
    id: Int,
): RegistrationCode? = newSuspendedTransaction {
    getCodeByCondition { RegistrationCodes.id eq id }
}

suspend fun getCodeByCode(
    code: String,
): RegistrationCode? = newSuspendedTransaction {
    getCodeByCondition { RegistrationCodes.code eq code }
}

suspend fun doesCodeExist(
    code: String,
): Boolean = newSuspendedTransaction { getCodeByCode(code) != null }

suspend fun insertCode(
    registrationCode: RegistrationCode,
): Boolean = newSuspendedTransaction {
    if (!doesCodeExist(registrationCode.code))
        RegistrationCodes.insert {
            it[code] = registrationCode.code
            it[totalUses] = registrationCode.totalUses
            it[remainingUses] = registrationCode.remainingUses
        }.insertedCount == 1
    else false
}

suspend fun insertCodes(
    registrationCodes: List<RegistrationCode>,
): List<Boolean> = newSuspendedTransaction {
    registrationCodes.map { insertCode(it) }
}
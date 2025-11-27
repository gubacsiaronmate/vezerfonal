package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.RegistrationCode
import com.smokinggunstudio.vezerfonal.objects.RegistrationCodes
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

private fun ResultRow.toCode(): RegistrationCode = RegistrationCode(
    id = this[RegistrationCodes.id],
    code = this[RegistrationCodes.code],
    totalUses = this[RegistrationCodes.totalUses],
    remainingUses = this[RegistrationCodes.remainingUses]
)

suspend fun getAllCodes(): List<RegistrationCode> =
    suspendTransaction {
        RegistrationCodes.selectAll().map { it.toCode() }
    }

suspend fun getCodeByCondition(
    condition: SQLCondition
): RegistrationCode? = suspendTransaction {
    RegistrationCodes.select(condition).toList().ifNotEmpty()?.single()?.toCode()
}

suspend fun getCodesByCondition(
    condition: SQLCondition
): List<RegistrationCode> = suspendTransaction {
    RegistrationCodes.select(condition).map { it.toCode() }
}

suspend fun getCodeById(
    id: Int,
): RegistrationCode? = suspendTransaction {
    getCodeByCondition { RegistrationCodes.id eq id }
}

suspend fun getCodeByCode(
    code: String,
): RegistrationCode? = suspendTransaction {
    getCodeByCondition { RegistrationCodes.code eq code }
}

suspend fun doesCodeExist(
    code: String,
): Boolean = suspendTransaction { getCodeByCode(code) != null }

suspend fun insertCode(
    registrationCode: RegistrationCode,
): Boolean = suspendTransaction {
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
): List<Boolean> = suspendTransaction {
    registrationCodes.map { insertCode(it) }
}
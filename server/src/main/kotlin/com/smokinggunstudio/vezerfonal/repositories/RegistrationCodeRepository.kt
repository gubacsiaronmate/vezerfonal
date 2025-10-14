package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.RegistrationCode
import com.smokinggunstudio.vezerfonal.objects.RegistrationCodes
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllCodes(context: CoroutineContext):List<RegistrationCode> = withContext(context) {
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
    condition: (RegistrationCode) -> Boolean
): RegistrationCode? = getAllCodes(context).firstOrNull(condition)

suspend fun getCodesByCondition(
    context: CoroutineContext,
    condition: (RegistrationCode) -> Boolean
): List<RegistrationCode> = getAllCodes(context).filter(condition)

suspend fun getCodeById(
    id: Int,
    context: CoroutineContext
): RegistrationCode? = getCodeByCondition(context) { code -> code.id == id }

suspend fun getCodeByCode(
    code: String,
    context: CoroutineContext
): RegistrationCode? = getCodeByCondition(context) { registrationCode -> registrationCode.code == code }
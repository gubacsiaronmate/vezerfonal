package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.models.JWTModel
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.repositories.doesCodeExist
import com.smokinggunstudio.vezerfonal.repositories.getCodeByCode
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun UserData.toUser(context: CoroutineContext): User = withContext(context) {
    val code = if(doesCodeExist(registrationCode, context))
        getCodeByCode(registrationCode, context)!!
    else error("Code cannot be null!")
    
    User(
        registrationCode = code,
        email = email,
        displayName = name,
        identifier = identifier,
        isSuperAdmin = isSuperAdmin,
        
        id = null,
        profilePic = null,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    ).let { user ->
        user.password = password!!
        user
    }
}
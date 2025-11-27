package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val registrationCodeId = integer("registration_code_id").references(RegistrationCodes.id)
    
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val profilePicURI = varchar("profile_pic_uri", 255).uniqueIndex().nullable()
    val displayName = varchar("display_name", 255)
    val identifier = varchar("identifier", 255).uniqueIndex()
    val isSuperAdmin = bool("is_super_admin").default(false)
    
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}
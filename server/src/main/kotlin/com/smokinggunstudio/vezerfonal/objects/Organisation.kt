package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.util.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Organisation : Table("organisation") {
    val name = varchar("name", 255)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}
package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.sql.Table

object UserNotificationSettings : Table("user_notification_settings") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id).uniqueIndex()
    val allowPush = bool("allow_push").default(true)
    val allowAlarm = bool("allow_alarm").default(true)
    
    override val primaryKey = PrimaryKey(id)
}
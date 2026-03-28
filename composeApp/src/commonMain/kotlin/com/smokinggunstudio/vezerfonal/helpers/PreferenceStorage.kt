package com.smokinggunstudio.vezerfonal.helpers

import com.russhwolf.settings.Settings
import com.smokinggunstudio.vezerfonal.enums.MessageStatus

class PreferenceStorage {
    private val settings = Settings()

    fun saveLanguage(languageTag: String) = settings.putString("language", languageTag)
    fun getLanguage(): String? = settings.getStringOrNull("language")
    fun saveTheme(isDark: Boolean) = settings.putBoolean("dark_theme", isDark)
    fun getTheme(): Boolean? = settings.getBooleanOrNull("dark_theme")

    fun saveArchiveEnabled(enabled: Boolean) = settings.putBoolean("archive_auto_enabled", enabled)
    fun getArchiveEnabled(): Boolean = settings.getBooleanOrNull("archive_auto_enabled") ?: false

    fun saveArchiveMinStatus(status: MessageStatus) = settings.putString("archive_min_status", status.name)
    fun getArchiveMinStatus(): MessageStatus {
        val stored = settings.getStringOrNull("archive_min_status")
            ?.let { name -> MessageStatus.entries.find { it.name == name } }
            ?: MessageStatus.read
        return if (stored == MessageStatus.sent) MessageStatus.read else stored
    }

    fun saveArchiveDelayHours(hours: Int) = settings.putInt("archive_delay_hours", hours)
    fun getArchiveDelayHours(): Int = settings.getIntOrNull("archive_delay_hours") ?: 168
}

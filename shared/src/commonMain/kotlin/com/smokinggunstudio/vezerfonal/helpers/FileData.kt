package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val name: String,
    val bytes: ByteArray,
    val mimeType: String,
    val fileType: String = name.substringAfterLast(".").lowercase()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        
        other as FileData
        
        if (name != other.name) return false
        if (!bytes.contentEquals(other.bytes)) return false
        if (mimeType != other.mimeType) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bytes.contentHashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}
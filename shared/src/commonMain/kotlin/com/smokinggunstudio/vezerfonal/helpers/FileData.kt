package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val bytes: ByteArray,
    val metaData: FileMetaData
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        
        other as FileData
        
        if (!bytes.contentEquals(other.bytes)) return false
        if (metaData != other.metaData) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + metaData.hashCode()
        return result
    }
    
}
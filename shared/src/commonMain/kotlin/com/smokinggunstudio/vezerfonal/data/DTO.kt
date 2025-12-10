package com.smokinggunstudio.vezerfonal.data

interface DTO {
    fun toSerializable(): Map<String, Any?>
}
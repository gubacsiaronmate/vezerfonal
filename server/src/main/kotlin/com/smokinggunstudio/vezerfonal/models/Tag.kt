package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.TagData

data class Tag(
    val id: Int?,
    val name: String,
) {
    fun toDTO() = TagData(name = name)
}

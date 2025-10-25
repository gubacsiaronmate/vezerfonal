package com.smokinggunstudio.vezerfonal.helpers



data class ProfileImage(
    override val uri: String?,
    val filename: String? = null
) : Image {
    companion object {
        fun fromURL(url: String): ProfileImage = ProfileImage(
            uri = url
        )
    }
}

package com.smokinggunstudio.vezerfonal.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
actual data class OrgData(
    actual override val name: String,
    actual val externalId: String
) : DTO, Parcelable
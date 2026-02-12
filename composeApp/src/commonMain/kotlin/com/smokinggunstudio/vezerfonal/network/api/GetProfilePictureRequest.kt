package com.smokinggunstudio.vezerfonal.network.api

import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData

expect suspend fun getProfilePicture(name: String = "", size: Int): FileData?
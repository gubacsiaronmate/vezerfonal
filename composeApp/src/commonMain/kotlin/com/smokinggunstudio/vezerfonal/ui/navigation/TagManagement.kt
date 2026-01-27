package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.ui.screens.TagManagementScreen

data class TagManagement(
    val accessToken: String,
    val tagListStr: List<String>
) : Screen {
    @Composable
    override fun Content() {
        TagManagementScreen(accessToken, tagListStr.map { it.toDTO<TagData>() })
    }
}
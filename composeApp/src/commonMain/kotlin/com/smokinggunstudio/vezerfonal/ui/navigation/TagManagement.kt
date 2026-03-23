package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.ui.screens.TagManagementScreen

data class TagManagement(
    val accessToken: String,
    val tagListStr: List<String>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        TagManagementScreen(
            accessToken = accessToken,
            tagsList = tagListStr.map { it.toDTO<TagData>() },
            onBack = { navigator.pop() },
        )
    }
}

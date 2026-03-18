package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.ui.screens.UserManagementScreen

data class UserManagement(
    val userListStr: List<String>
) : Screen {
    @Composable
    override fun Content() {
        UserManagementScreen(userListStr.map { it.toDTO<UserData>() })
    }
}

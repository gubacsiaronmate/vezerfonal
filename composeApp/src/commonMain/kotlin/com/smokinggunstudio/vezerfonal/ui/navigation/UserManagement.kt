package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.ui.screens.UserManagementScreen

data class UserManagement(
    val token: String,
    val userListStr: List<String>,
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow
        UserManagementScreen(
            users = userListStr.map { it.toDTO<UserData>() },
            accessToken = token,
            client = client,
            onBack = { navigator.pop() },
        )
    }
}

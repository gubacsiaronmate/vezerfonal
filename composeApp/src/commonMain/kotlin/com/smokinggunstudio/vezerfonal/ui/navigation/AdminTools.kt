package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.ui.screens.AdminToolsScreen
import io.ktor.client.HttpClient

class AdminTools(
    val token: String,
    val client: HttpClient,
    val regCodes: List<RegCodeData>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        AdminToolsScreen(
            onUserManagementClick = { navigator.push(UserManagement) },
            onTagManagementClick = { navigator.push(TagManagement) },
            onRegistrationCodeManagementClick = {
                navigator.push(
                    RegCodeManagement(
                        token = token,
                        client = client,
                        regCodes = regCodes
                    )
                )
            }
        )
    }
}
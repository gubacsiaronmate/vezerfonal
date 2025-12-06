package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.ui.screens.RegCodeManagementScreen
import io.ktor.client.HttpClient

class RegCodeManagement(
    val token: String,
    val client: HttpClient,
    val regCodes: List<RegCodeData>
) : Screen {
    @Composable
    override fun Content() {
        RegCodeManagementScreen(
            client = client,
            accessToken = token,
            registrationCodes = regCodes
        )
    }
}
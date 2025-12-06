package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.ui.screens.RegCodeManagementScreen
import io.ktor.client.HttpClient

data class RegCodeManagement(
    val token: String,
    val regCodes: List<RegCodeData>
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        
        RegCodeManagementScreen(client, token, regCodes)
    }
}
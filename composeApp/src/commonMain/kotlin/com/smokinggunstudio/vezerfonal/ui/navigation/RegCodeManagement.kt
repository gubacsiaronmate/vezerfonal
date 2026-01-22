package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.ui.screens.RegCodeManagementScreen
import io.ktor.client.HttpClient

data class RegCodeManagement(
    val token: String,
    val regCodesStr: List<String>
) : Screen {
    @Composable
    override fun Content() {
        RegCodeManagementScreen(token, regCodesStr.map { it.toDTO<RegCodeData>() })
    }
}
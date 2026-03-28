package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.ui.screens.RegCodeManagementScreen

data class RegCodeManagement(
    val token: String,
    val regCodesStr: List<String>
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        RegCodeManagementScreen(
            accessToken = token,
            registrationCodes = regCodesStr.map { it.toDTO<RegCodeData>() },
            onBack = { navigator.pop() },
        )
    }
}

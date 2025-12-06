package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.ui.screens.ChangePasswordScreen

data object ChangePassword : Screen {
    @Composable
    override fun Content() = ChangePasswordScreen()
}
package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.ui.helpers.toModel
import com.smokinggunstudio.vezerfonal.ui.helpers.toSerialized
import com.smokinggunstudio.vezerfonal.ui.screens.CreateOrganizationScreen
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel

data class CreateOrg(
    val snapshot: String,
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow
        
        CreateOrganizationScreen(snapshot.toModel()) {
            navigator.push(Register(2, it.toSerialized()))
        }
    }
}
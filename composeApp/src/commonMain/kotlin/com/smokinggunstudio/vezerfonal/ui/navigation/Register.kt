package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.LocalTokenStorage
import com.smokinggunstudio.vezerfonal.ui.screens.CredentialsRegisterScreen
import com.smokinggunstudio.vezerfonal.ui.screens.InitialRegisterScreen
import com.smokinggunstudio.vezerfonal.ui.screens.ProfileCreationScreen
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import kotlinx.coroutines.launch

data class Register(
    val page: Int,
    val state: RegisterStateModel = RegisterStateModel.NonAdminRegisterStateModel(),
) : Screen {
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        
        when (page) {
            1 -> InitialRegisterScreen(
                onCreateOrgClick = {
                    navigator.replace(CreateOrg(RegisterStateModel.AdminRegisterStateModel()))
                },
                onContinueClick = {
                    navigator.push(Register(2, it))
                },
            )
            2 -> CredentialsRegisterScreen(state) {
                navigator.push(Register(3, it))
            }
            3 -> ProfileCreationScreen(state, client) { tokens ->
                scope.launch { tokenStorage.saveTokens(tokens) }
                navigator.push(Home(tokens.accessToken))
            }
        }
        
    }
}
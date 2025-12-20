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
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import kotlinx.coroutines.launch

data class Register(
    val page: Int,
    val regState: RegisterState?,
) : Screen {
    lateinit var registerState: RegisterState
    
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val client = LocalHttpClient.current
        val tokenStorage = LocalTokenStorage.current
        val navigator = LocalNavigator.currentOrThrow
        
        when (page) {
            1 -> InitialRegisterScreen(
                onContinueClick = { navigator.push(Register(2, regState ?: registerState)) },
                onCreateOrgClick = {
                    navigator.replace(CreateOrg((regState ?: registerState) as AdminRegisterState))
                },
                returnRegState = { registerState = it },
            ) 
            2 -> CredentialsRegisterScreen(regState ?: registerState) {
                navigator.push(Register(3, regState ?: registerState))
            }
            3 -> ProfileCreationScreen(regState ?: registerState, client) { tokens ->
                scope.launch { tokenStorage.saveTokens(tokens) }
                navigator.push(HomePage(tokens.accessToken))
            }
        }
        
    }
}
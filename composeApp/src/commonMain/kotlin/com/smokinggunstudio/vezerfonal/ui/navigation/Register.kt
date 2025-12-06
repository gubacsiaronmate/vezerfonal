package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.CreateOrganizationScreen
import com.smokinggunstudio.vezerfonal.ui.screens.CredentialsRegisterScreen
import com.smokinggunstudio.vezerfonal.ui.screens.InitialRegisterScreen
import com.smokinggunstudio.vezerfonal.ui.screens.ProfileCreationScreen
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import io.ktor.client.HttpClient
import io.ktor.client.plugins.convertLongTimeoutToIntWithInfiniteAsZero
import kotlinx.coroutines.launch

class Register(
    val page: Int,
    val client: HttpClient,
    val isDarkMode: Boolean?,
    val regState: RegisterState?,
    val tokenStorage: TokenStorage,
    val darkModeStateCallback: CallbackEvent<Boolean>
) : Screen {
    lateinit var registerState: RegisterState
    
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        
        when (page) {
            1 -> InitialRegisterScreen(
                onContinueClick = {
                    navigator.push(
                        Register(
                            page = 2,
                            client = client,
                            isDarkMode = isDarkMode,
                            regState = regState ?: registerState,
                            tokenStorage = tokenStorage,
                            darkModeStateCallback = darkModeStateCallback
                        )
                    )
                },
                onCreateOrgClick = {
                    navigator.replace(
                        CreateOrg(
                            client = client,
                            isDarkMode = isDarkMode,
                            tokenStorage = tokenStorage,
                            registerState = (regState ?: registerState) as AdminRegisterState,
                            darkModeStateCallback = darkModeStateCallback
                        )
                    )
                },
                returnRegState = { registerState = it },
            ) 
            2 -> CredentialsRegisterScreen(regState ?: registerState) {
                navigator.push(
                    Register(
                        page = 3,
                        client = client,
                        isDarkMode = isDarkMode,
                        regState = regState ?: registerState,
                        tokenStorage = tokenStorage,
                        darkModeStateCallback = darkModeStateCallback
                    )
                )
            }
            3 -> ProfileCreationScreen(regState ?: registerState, client) { tokens ->
                scope.launch { tokenStorage.saveTokens(tokens) }
                navigator.push(
                    Home(
                        accessToken = tokens.accessToken,
                        client = client,
                        isDarkMode = isDarkMode,
                        tokenStorage = tokenStorage,
                        darkModeStateSwitch = darkModeStateCallback
                    )
                )
            }
        }
        
    }
}
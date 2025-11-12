package com.smokinggunstudio.vezerfonal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.smokinggunstudio.vezerfonal.network.client.createHttpClient
import com.smokinggunstudio.vezerfonal.ui.helpers.NavTree
import com.smokinggunstudio.vezerfonal.ui.helpers.go
import com.smokinggunstudio.vezerfonal.ui.helpers.route
import com.smokinggunstudio.vezerfonal.ui.helpers.screen
import com.smokinggunstudio.vezerfonal.ui.screens.*
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.NonAdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import com.smokinggunstudio.vezerfonal.ui.theme.VezerfonalTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable fun App() {
    VezerfonalTheme {
        PreComposeApp {
            Navigator()
        }
    }
}


@Composable fun Navigator() {
    val navigator = rememberNavigator()
    var registerState by mutableStateOf<RegisterState?>(null)
    val client = createHttpClient()
    
    NavHost(navigator = navigator, initialRoute = NavTree.Landing.route) {
        screen(NavTree.Landing) {
            LandingPageScreen(
                onRegisterClick = { navigator.go(NavTree.Register(1)) },
                onLoginClick = { navigator.go(NavTree.Login) }
            )
        }
        
        screen(NavTree.Home) { HomePageScreen() }
        
        screen(NavTree.Register(1)) {
            fun handleOnClickCallback(regState: RegisterState) {
                registerState = regState
                when (registerState) {
                    is NonAdminRegisterState -> navigator.go(NavTree.Register(2))
                    is AdminRegisterState -> navigator.go(NavTree.CreateOrg)
                    else -> error(
                        "handleOnClickCallback: registerState has a weird type: { ${registerState!!::class.simpleName} } or value: { $registerState }"
                    )
                }
            }
            
            InitialRegisterScreen(::handleOnClickCallback)
        }
        
        screen(NavTree.CreateOrg) {
            if (registerState == null)
                error("Register2.route: RegisterState cannot be null.")
            
            // TODO: Organisation creation screen
        }
        
        screen(NavTree.Register(2)) {
            if (registerState == null)
                error("Register2.route: RegisterState cannot be null.")
            
            CredentialsRegisterScreen(registerState!!) { navigator.go(NavTree.Register(3)) }
        }
        
        screen(NavTree.Register(3)) {
            if (registerState == null)
                error("Register2.route: RegisterState cannot be null.")
            
            ProfileCreationScreen(registerState!!, client) { navigator.go(NavTree.Home) }
        }
        
        screen(NavTree.Login) { LoginScreen(client) { navigator.go(NavTree.Home) } }
    }
}

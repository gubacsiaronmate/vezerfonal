package com.smokinggunstudio.vezerfonal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.helpers.NavTree
import com.smokinggunstudio.vezerfonal.helpers.goTo
import com.smokinggunstudio.vezerfonal.ui.screens.FirstRegisterScreen
import com.smokinggunstudio.vezerfonal.ui.screens.ProfileCreationScreen
import com.smokinggunstudio.vezerfonal.ui.screens.SecondRegisterScreen
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
    val registerState = mutableStateOf<RegisterState?>(null)
    
    NavHost(navigator = navigator, initialRoute = NavTree.Landing.route) {
        scene(NavTree.Landing.route) { navigator.goTo(NavTree.Register1) }
        
        scene(NavTree.Home.route) { navigator.goTo(NavTree.Register1) }
        
        scene(NavTree.Register1.route) {
            fun handleOnClickCallback(regState: RegisterState?) {
                registerState.value = regState
                when (registerState.value) {
                    is NonAdminRegisterState -> navigator.goTo(NavTree.Register2)
                    is AdminRegisterState -> navigator.goTo(NavTree.CreateOrg)
                    null -> error("goToCorrectNav: registerState.value cannot be null")
                    else -> error(
                        "goToCorrectNav: registerState.value has a weird type: { ${registerState.value!!::class.simpleName} } or value: { ${registerState.value} }"
                    )
                }
            }
            
            FirstRegisterScreen(
                onClickCallbackNonAdmin = ::handleOnClickCallback,
                onClickCallbackAdmin = ::handleOnClickCallback
            )
        }
        
        scene(NavTree.CreateOrg.route) {
            if (registerState.value == null)
                error("Register2.route: RegisterState cannot be null.")
            
            // TODO: Organisation creation screen
        }
        
        scene(NavTree.Register2.route) {
            if (registerState.value == null)
                error("Register2.route: RegisterState cannot be null.")
            
            SecondRegisterScreen(registerState.value!!) { navigator.goTo(NavTree.Register3) }
        }
        
        scene(NavTree.Register3.route) {
            if (registerState.value == null)
                error("Register2.route: RegisterState cannot be null.")
            
            ProfileCreationScreen(registerState.value!!) {
                
                navigator.goTo(NavTree.Home)
            }
        }
    }
}

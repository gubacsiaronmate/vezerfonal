package com.smokinggunstudio.vezerfonal

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.Archive
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.Group
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.Home
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.Send
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.Settings
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.getUserData
import com.smokinggunstudio.vezerfonal.network.client.createHttpClient
import com.smokinggunstudio.vezerfonal.ui.components.NavBar
import com.smokinggunstudio.vezerfonal.ui.helpers.NavTree
import com.smokinggunstudio.vezerfonal.ui.helpers.capitalize
import com.smokinggunstudio.vezerfonal.ui.helpers.go
import com.smokinggunstudio.vezerfonal.ui.helpers.route
import com.smokinggunstudio.vezerfonal.ui.helpers.screen
import com.smokinggunstudio.vezerfonal.ui.screens.*
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.NonAdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import com.smokinggunstudio.vezerfonal.ui.theme.VezerfonalTheme
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.archive
import vezerfonal.composeapp.generated.resources.groups
import vezerfonal.composeapp.generated.resources.home
import vezerfonal.composeapp.generated.resources.send_message
import vezerfonal.composeapp.generated.resources.settings

@Composable fun App() {
    VezerfonalTheme {
        PreComposeApp {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) { Navigator() }
        }
    }
}


@Composable fun Navigator() {
    val navigator = rememberNavigator()
    var registerState by remember { mutableStateOf<RegisterState?>(null) }
    var pendingRegisterState by remember {  mutableStateOf<RegisterState?>(null) }
    val client = createHttpClient()
    val canGoBack by navigator.canGoBack.collectAsState(initial = false)
    val tokenStorage = TokenStorage()
    
    LaunchedEffect(pendingRegisterState) {
        pendingRegisterState?.let { regState ->
            registerState = regState
            when (registerState) {
                is NonAdminRegisterState -> navigator.go(NavTree.Register(2))
                is AdminRegisterState -> navigator.go(NavTree.CreateOrg)
                else -> error(
                    "handleOnClickCallback: registerState has a weird type: { ${registerState!!::class.simpleName} } or value: { $registerState }"
                )
            }
        }
    }
    
    BackHandler(enabled = canGoBack)
    { navigator.goBack() }
    
    NavHost(navigator = navigator, initialRoute = NavTree.Landing.route) {
        screen(NavTree.Landing) {
            var tokens: TokenResponse? by remember { mutableStateOf(null) }
            var loaded by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                val t = tokenStorage.getTokens()
                tokens = t
                loaded = true
            }
            
            if (!loaded) return@screen
            
            if (tokens == null) LandingPageScreen(
                onRegisterClick = { navigator.go(NavTree.Register(1)) },
                onLoginClick = { navigator.go(NavTree.Login) },
                myTestClickEvent = { navigator.go(NavTree.Home) }
            ) else navigator.go(NavTree.Home)
        }
        
        screen(NavTree.Home) { MainTabHost(tokenStorage, client) }
        
        screen(NavTree.Register(1)) {
            InitialRegisterScreen { pendingRegisterState = it }
        }
        
        screen(NavTree.CreateOrg) {
            if (registerState == null)
                error("CreateOrg.route: RegisterState cannot be null.")
            
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
            
            ProfileCreationScreen(registerState!!, tokenStorage, client) { navigator.go(NavTree.Home) }
        }
        
        screen(NavTree.Login) { LoginScreen(client, tokenStorage) { navigator.go(NavTree.Home) } }
    }
}

@Composable private fun MainTabHost(
    tokenStorage: TokenStorage,
    client: HttpClient
) {
    var userData: UserData? by remember { mutableStateOf(null) }
    var loaded by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        val u = getUserData(tokenStorage, client)
        userData = u
        loaded = true
    }
    
    if (!loaded) return
    
    if (userData == null) error("UserData is null.")
    
    val tabs = remember {
        buildList {
            add(Home)
            add(Archive)
            if (userData!!.isAnyAdmin) add(Send)
            add(Group)
            add(Settings)
        }
    }
    
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()
    
    Scaffold(
        bottomBar = {
            NavBar(
                tabs = tabs,
                currentIndex = pagerState.currentPage,
                onTabSelected = { i ->
                    scope.launch { pagerState.animateScrollToPage(i) }
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues),
            userScrollEnabled = true
        ) { i ->
            when (tabs[i]) {
                Home -> HomePageScreen(tokenStorage, client)
                Archive -> ArchiveScreen()
                Send -> WriteMessageScreen()
                Group -> GroupScreen()
                Settings -> SettingsScreen()
            }
        }
    }
}
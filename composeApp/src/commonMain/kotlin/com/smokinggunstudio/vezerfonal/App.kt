package com.smokinggunstudio.vezerfonal

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.*
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.getGroupData
import com.smokinggunstudio.vezerfonal.network.api.getUserData
import com.smokinggunstudio.vezerfonal.network.client.createHttpClient
import com.smokinggunstudio.vezerfonal.network.helpers.getAccessToken
import com.smokinggunstudio.vezerfonal.ui.components.NavBar
import com.smokinggunstudio.vezerfonal.ui.helpers.NavTree
import com.smokinggunstudio.vezerfonal.ui.helpers.go
import com.smokinggunstudio.vezerfonal.ui.helpers.route
import com.smokinggunstudio.vezerfonal.ui.helpers.screen
import com.smokinggunstudio.vezerfonal.ui.screens.*
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.NonAdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import com.smokinggunstudio.vezerfonal.ui.theme.VezerfonalTheme
import io.ktor.client.*
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import com.smokinggunstudio.vezerfonal.ui.helpers.BackHandler

@Composable fun App() {
    VezerfonalTheme {
        PreComposeApp {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) { NavigatorComposable() }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable fun NavigatorComposable() {
    val navigator = rememberNavigator()
    val scope = rememberCoroutineScope()
    var registerState by remember { mutableStateOf<RegisterState?>(null) }
    var pendingRegisterState by remember {  mutableStateOf<RegisterState?>(null) }
    val client = createHttpClient()
    val tokenStorage = remember {
        try {
            TokenStorage()
        } catch (e: Exception) {
            throw e
        }
    }
    var token: String? by remember { mutableStateOf(null) }
    var orgs: List<OrgData> by remember { mutableStateOf(emptyList()) }
    
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
    
    BackHandler.Bind(navigator)
    
    NavHost(navigator = navigator, initialRoute = NavTree.Landing.route) {
        screen(NavTree.Landing) {
            var loaded by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                val t = getAccessToken(tokenStorage, client)
                token = t
                loaded = true
            }
            
            if (!loaded) return@screen
            
            if (token == null) LandingPageScreen(
                onRegisterClick = { navigator.go(NavTree.Register(1)) },
                onLoginClick = { data ->
                    orgs = data
                    navigator.go(NavTree.Login)
                },
                asdClickEvent = { navigator.go(NavTree.Test) }
            ) else navigator.go(NavTree.Home)
        }
        
        screen(NavTree.Home) {
            if (token == null) return@screen
            MainTabHost(token!!, navigator, client)
        }
        
        screen(NavTree.Register(1)) {
            InitialRegisterScreen { pendingRegisterState = it }
        }
        
        screen(NavTree.CreateOrg) {
            if (registerState == null)
                error("CreateOrg.route: RegisterState cannot be null.")
            
            CreateOrganizationScreen(
                registerState as AdminRegisterState, client
            ) { navigator.go(NavTree.Register(2)) }
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
        
        screen(NavTree.Login) {
            LoginScreen(client, orgs) { newTokens ->
                token = newTokens.accessToken
                navigator.go(NavTree.Home)
                scope.launch {
                    tokenStorage.saveTokens(newTokens)
                }
            }
        }
        
        screen(NavTree.AccountSettings) {
            if (token == null) {
                navigator.go(NavTree.Landing)
                return@screen
            }
            AccountSettingsScreen(client, token!!, tokenStorage) {
                navigator.go(NavTree.Landing)
            }
        }
        
        screen(NavTree.Test) { GroupScreen(emptyList()) }
    }
}

@Composable private fun MainTabHost(
    accessToken: String,
    navigator: Navigator,
    client: HttpClient
) {
    var userData: UserData? by remember { mutableStateOf(null) }
    var groupData by remember { mutableStateOf<List<GroupData>?>(null) }
    var loaded by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        val u = getUserData(accessToken, client)
        val g = getGroupData(accessToken, client)
        userData = u
        groupData = g
        loaded = true
    }
    
    if (!loaded) return
    
    if (userData == null) error("UserData is null.")
    if (groupData == null) error("GroupData is null.")
    
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
    var isScrollEnabled by remember { mutableStateOf(true) }
    
    Scaffold(
        bottomBar = {
            NavBar(
                tabs = tabs,
                currentIndex = pagerState.currentPage,
                onTabSelected = { i -> scope.launch { pagerState.animateScrollToPage(i) } }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues),
            userScrollEnabled = isScrollEnabled
        ) { i ->
            when (tabs[i]) {
                Home -> HomePageScreen(accessToken, client) { isScrollEnabled = !it }
                Archive -> ArchiveScreen()
                Send -> WriteMessageScreen()
                Group -> {
                    if (!userData!!.isSuperAdmin)
                        GroupScreen(groupData!!)
                    else SuperAdminGroupScreen()
                }
                Settings -> SettingsScreen { navigator.go(NavTree.AccountSettings) }
            }
        }
    }
}
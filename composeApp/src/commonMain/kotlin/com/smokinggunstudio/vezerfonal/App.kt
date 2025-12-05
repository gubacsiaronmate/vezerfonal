package com.smokinggunstudio.vezerfonal

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.*
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.getAllGroupsUserIsAdminOf
import com.smokinggunstudio.vezerfonal.network.api.getAllOrgsRequest
import com.smokinggunstudio.vezerfonal.network.api.getAllRegCodes
import com.smokinggunstudio.vezerfonal.network.api.getAllTags
import com.smokinggunstudio.vezerfonal.network.api.getGroupData
import com.smokinggunstudio.vezerfonal.network.api.getUserData
import com.smokinggunstudio.vezerfonal.network.api.getUsersByIdentifierList
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
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent

@Composable fun App() {
    var darkModeState by remember { mutableStateOf<Boolean?>(null) }
    VezerfonalTheme(darkTheme = darkModeState ?: isSystemInDarkTheme()) {
        PreComposeApp {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) { NavigatorComposable(darkModeState) { darkModeState = it } }
        }
    }
}


@Composable fun NavigatorComposable(
    isDarkMode: Boolean?,
    darkModeStateCallback: CallbackEvent<Boolean>
) {
    val navigator = rememberNavigator()
    val scope = rememberCoroutineScope()
    lateinit var registerState: RegisterState
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
    lateinit var orgs: List<OrgData>/* by remember { mutableStateOf(emptyList()) }*/
    lateinit var regCodes: List<RegCodeData>/*? by remember { mutableStateOf(null) }*/
    lateinit var user: UserData/*? by remember { mutableStateOf(null) }*/
    var message by remember { mutableStateOf<MessageData?>(null) }
    
    LaunchedEffect(pendingRegisterState) {
        pendingRegisterState?.let { regState ->
            registerState = regState
            when (registerState) {
                is NonAdminRegisterState -> navigator.go(NavTree.Register(2))
                is AdminRegisterState -> navigator.go(NavTree.CreateOrg)
                else -> error(
                    "handleOnClickCallback: registerState has a weird type: { ${registerState::class.simpleName} } or value: { $registerState }"
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
                val o = getAllOrgsRequest(client)
                token = t
                orgs = o
                loaded = true
            }
            
            if (!loaded) return@screen
            
            if (token != null) navigator.go(NavTree.Home)
            
            LandingPageScreen(
                onRegisterClick = { navigator.go(NavTree.Register(1)) },
                onLoginClick = { navigator.go(NavTree.Login) },
            )
        }
        
        screen(NavTree.Home) {
            if (token == null) return@screen
            MainTabHost(
                accessToken = token!!,
                navigator = navigator,
                client = client,
                isDarkMode = isDarkMode,
                darkModeStateSwitch = darkModeStateCallback,
                returnRegCodes = { regCodes = it },
                returnUser = { user = it },
                returnMessage = { message = it }
            )
        }
        
        screen(NavTree.Register(1)) {
            InitialRegisterScreen { pendingRegisterState = it }
        }
        
        screen(NavTree.CreateOrg) {
            CreateOrganizationScreen(
                registerState as AdminRegisterState, client
            ) { navigator.go(NavTree.Register(2)) }
        }
        
        screen(NavTree.Register(2)) {
            CredentialsRegisterScreen(registerState) { navigator.go(NavTree.Register(3)) }
        }
        
        screen(NavTree.Register(3)) {
            ProfileCreationScreen(registerState, tokenStorage, client) { navigator.go(NavTree.Home) }
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
            if (token == null)
                return@screen navigator.go(NavTree.Landing)
            
            AccountSettingsScreen(
                user = user,
                client = client,
                accessToken = token!!,
                tokenStorage = tokenStorage,
                onLogOutClick = { navigator.go(NavTree.Landing) },
                onChangePasswordClick = { navigator.go(NavTree.ChangePassword) }
            )
        }
        
        screen(NavTree.AdminTools) {
            AdminToolsScreen(
                onUserManagementClick = { navigator.go(NavTree.UserManagement) },
                onTagManagementClick = { navigator.go(NavTree.TagManagement) },
                onRegistrationCodeManagementClick = { navigator.go(NavTree.RegCodeManagement) }
            )
        }
        
        screen(NavTree.RegCodeManagement) {
            if (token == null)
                return@screen navigator.go(NavTree.Landing)
            
            RegCodeManagementScreen(
                client = client,
                accessToken = token!!,
                registrationCodes = regCodes
            )
        }
        
        screen(NavTree.TagManagement) { }
        
        screen(NavTree.UserManagement) { }
        
        screen(NavTree.ChangePassword) { ChangePasswordScreen() }
        
        screen(NavTree.ViewMessage) { MessageViewScreen(message!!) }
    }
}

@Composable private fun MainTabHost(
    accessToken: String,
    navigator: Navigator,
    client: HttpClient,
    isDarkMode: Boolean?,
    darkModeStateSwitch: CallbackEvent<Boolean>,
    returnRegCodes: CallbackEvent<List<RegCodeData>>,
    returnUser: CallbackEvent<UserData>,
    returnMessage: CallbackEvent<MessageData>
) {
    var loaded by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf<UserData?>(null) }
    var groups by remember { mutableStateOf<List<GroupData>?>(null) }
    var regCodes by remember { mutableStateOf<List<RegCodeData>?>(null) }
    var guiao by remember { mutableStateOf<List<GroupData>>(emptyList()) }
    var userList by remember { mutableStateOf<List<UserData>>(emptyList()) }
    var tagList by remember { mutableStateOf<List<TagData>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        user = getUserData(accessToken, client)
        
        groups = getGroupData(accessToken, client)
        
        if (user!!.isSuperAdmin)
            regCodes = getAllRegCodes(accessToken, client)
        
        if (user!!.isAnyAdmin)
            guiao = getAllGroupsUserIsAdminOf(accessToken, client)
        
        userList = getUsersByIdentifierList(
            guiao
                .map { it.members }
                .flatten()
                .distinct(),
            accessToken,
            client
        )
        
        tagList = getAllTags(accessToken, client)
        
        loaded = true
    }
    
    if (!loaded) return
    
    if (user!!.isSuperAdmin)
        returnRegCodes(regCodes!!)
    returnUser(user!!)
    
    val tabs = remember {
        buildList {
            add(Home)
            add(Archive)
            if (user!!.isAnyAdmin || user!!.isSuperAdmin) add(Send)
            add(Group)
            add(Settings)
        }
    }
    
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { tabs.size }
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
                Home -> HomePageScreen(
                    accessToken = accessToken,
                    client = client,
                    onMessageClick = {
                        returnMessage(it)
                        navigator.go(NavTree.ViewMessage)
                    },
                    scrollLockedBySliderCallback = { isScrollEnabled = !it }
                )
                Archive -> ArchiveScreen()
                Send -> WriteMessageScreen(user!!, client, accessToken, guiao, userList, tagList)
                Group -> GroupScreen(client, accessToken, user!!.identifier, groups!!, user!!.isSuperAdmin)
                Settings -> SettingsScreen(
                    username = user!!.name,
                    isInDarkTheme = isDarkMode ?: isSystemInDarkTheme(),
                    onAccountSettingsClick = { navigator.go(NavTree.AccountSettings) },
                    isSuperAdminLogIn = user!!.isSuperAdmin,
                    onAdminToolsClick = { navigator.go(NavTree.AdminTools) },
                    onArchiveClick = { },
                    onNotificationsClick = { },
                    onTOSClick = { },
                    onLanguageClick = { },
                    onThemeSwitchClick = darkModeStateSwitch,
                )
            }
        }
    }
}
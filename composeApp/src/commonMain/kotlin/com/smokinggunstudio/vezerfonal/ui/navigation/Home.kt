package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalDarkModeState
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.*
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.*
import com.smokinggunstudio.vezerfonal.ui.components.NavBar
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.*
import io.ktor.client.*
import kotlinx.coroutines.launch

data class Home(
    val accessToken: String,
) : Screen {
    @Composable
    @OptIn(ExperimentalFoundationApi::class)
    override fun Content() {
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow
        val darkModeState = LocalDarkModeState.current
        var loaded by remember { mutableStateOf(false) }
        var user by remember { mutableStateOf<UserData?>(null) }
        var groups by remember { mutableStateOf<List<GroupData>?>(null) }
        var guiao by remember { mutableStateOf<List<GroupData>>(emptyList()) }
        var tagList by remember { mutableStateOf<List<TagData>>(emptyList()) }
        var userList by remember { mutableStateOf<List<UserData>>(emptyList()) }
        var regCodes by remember { mutableStateOf<List<RegCodeData>>(emptyList()) }
        
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
        
        if (!loaded) {
            Box(Modifier.fillMaxSize()) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
            return
        }
        
        val tabs = remember {
            buildList {
                add(NavBarContent.Home)
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
                    NavBarContent.Home -> HomePageScreen(
                        accessToken = accessToken,
                        client = client,
                        onMessageClick = { navigator.push(ViewMessage(it)) },
                        scrollLockedBySliderCallback = { isScrollEnabled = !it }
                    )
                    Archive -> ArchiveScreen()
                    Send -> WriteMessageScreen(user!!, client, accessToken, guiao, userList, tagList)
                    Group -> GroupScreen(client, accessToken, user!!.identifier, groups!!, user!!.isSuperAdmin)
                    Settings -> SettingsScreen(
                        user = user!!,
                        isInDarkTheme = darkModeState.value ?: isSystemInDarkTheme(),
                        onAccountSettingsClick = { navigator.push(AccountSettings(accessToken, user!!)) },
                        isSuperAdminLogIn = user!!.isSuperAdmin,
                        onAdminToolsClick = { navigator.push(AdminTools(accessToken, regCodes)) },
                        onArchiveClick = { },
                        onNotificationsClick = { },
                        onTOSClick = { },
                        onLanguageClick = { },
                        onThemeSwitchClick = { darkModeState.value = it },
                    )
                }
            }
        }
    }
}
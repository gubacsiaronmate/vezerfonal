package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.NavBar
import com.smokinggunstudio.vezerfonal.ui.helpers.HomeCache
import com.smokinggunstudio.vezerfonal.ui.screens.*
import kotlinx.coroutines.launch

data class Home(
    val accessToken: String,
) : Screen {
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow
        val darkModeState = LocalDarkModeState.current
        var loaded by remember { mutableStateOf(false) }
        var isRefreshing by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<Throwable?>(null) }
        
        var user by remember { mutableStateOf<UserData?>(null) }
        var groups by remember { mutableStateOf<List<GroupData>>(emptyList()) }
        var guiao by remember { mutableStateOf<List<GroupData>>(emptyList()) }
        var tagList by remember { mutableStateOf<List<TagData>>(emptyList()) }
        var userList by remember { mutableStateOf<List<UserData>>(emptyList()) }
        var regCodes by remember { mutableStateOf<List<RegCodeData>>(emptyList()) }

        suspend fun loadAll(force: Boolean = false) {
            if (force) HomeCache.invalidate()
            HomeCache.load(accessToken, client)

            HomeCache.user.let {
                if (it != null) user = it
                else throw UnableToLoadException()
            }
            groups = HomeCache.groups
            regCodes = HomeCache.regCodes
            guiao = HomeCache.guiao
            userList = HomeCache.userList
            tagList = HomeCache.tagList

            loaded = true
        }

        LaunchedEffect(Unit) {
            try {
                loadAll(force = false)
            } catch (e: UnableToLoadException) {
                error = e
            }
        }

        val pullRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            modifier = Modifier.fillMaxSize(),
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    try {
                        loadAll(force = true)
                    } catch (e: UnableToLoadException) {
                        error = e
                    }
                    isRefreshing = false
                }
            },
        ) {
            if (!loaded) {
                Box(Modifier.fillMaxSize()) { LinearProgressIndicator(Modifier.align(Alignment.Center)) }
                return@PullToRefreshBox
            }

            if (error != null || user == null)
                return@PullToRefreshBox Box(Modifier.fillMaxSize()) {
                    ErrorDialog(
                        errorMessage = error?.message ?: "User not found",
                        isUnauthed = false,
                        modifier = Modifier.align(Alignment.Center)
                    )
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
                        Archive -> ArchiveScreen(
                            onMessageClick = { navigator.push(ViewMessage(it)) },
                            scrollLockedBySliderCallback = { isScrollEnabled = !it }
                        )
                        Send -> WriteMessageScreen(user!!, client, accessToken, guiao, userList, tagList)
                        Group -> GroupScreen(client, accessToken, user!!.identifier, groups, user!!.isSuperAdmin)
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
}
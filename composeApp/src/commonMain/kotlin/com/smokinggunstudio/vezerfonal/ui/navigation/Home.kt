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
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent.Home
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UserNotFoundException
import com.smokinggunstudio.vezerfonal.helpers.toSerialized
import com.smokinggunstudio.vezerfonal.network.api.registerPushToken
import com.smokinggunstudio.vezerfonal.network.helpers.Platform
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.NavBar
import com.smokinggunstudio.vezerfonal.ui.helpers.HomeCache
import com.smokinggunstudio.vezerfonal.ui.screens.*
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging
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
                
                val token = Firebase.messaging.getToken()
                
                registerPushToken(
                    accessToken = accessToken,
                    client = client,
                    fcmToken = token,
                    platform = Platform.type.name.lowercase()
                )
            } catch (e: Exception) {
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
                    } catch (e: Exception) {
                        error = e
                    }
                    isRefreshing = false
                }
            },
        ) {
            if (!loaded) {
                Box(Modifier.fillMaxSize()) {
                    LinearProgressIndicator(Modifier.align(Alignment.Center))
                }
                return@PullToRefreshBox
            }

            val effectiveError = error
                ?: if (user != null) null
                else UserNotFoundException()
            
            if (effectiveError != null) {
                return@PullToRefreshBox Box(Modifier.fillMaxSize()) {
                    ErrorDialog(error!!, Modifier.align(Alignment.Center))
                }
            }

            val tabs: List<NavBarContent> = remember {
                buildList {
                    add(Home)
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
                        onTabSelected = { i ->
                            scope.launch {
                                pagerState.animateScrollToPage(i)
                            }
                        }
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
                            userIdentifier = user!!.externalId,
                            tagList = tagList,
                            onMessageClick = {
                                navigator.push(
                                    ViewMessage(
                                        accessToken = accessToken,
                                        isArchived = false,
                                        messageStr = it.toSerialized(),
                                        isSenderView = false,
                                        userIdentifier = user!!.externalId
                                    )
                                )
                            },
                            scrollLockedBySliderCallback = { isScrollEnabled = !it }
                        )
                        Archive -> ArchiveScreen(
                            accessToken = accessToken,
                            tagList = tagList,
                            onMessageClick = {
                                navigator.push(
                                    ViewMessage(
                                        accessToken = accessToken,
                                        isArchived = true,
                                        messageStr = it.toSerialized(),
                                        isSenderView = false,
                                        userIdentifier = user!!.externalId
                                    )
                                )
                            },
                            scrollLockedBySliderCallback = { isScrollEnabled = !it }
                        )
                        Send -> WriteMessageScreen(
                            user = user!!,
                            accessToken = accessToken,
                            guiao = guiao,
                            userList = userList,
                            tagList = tagList
                        )
                        Group -> GroupScreen(
                            accessToken = accessToken,
                            myIdentifier = user!!.externalId,
                            groupData = groups,
                            isSuperAdminLogIn = user!!.isSuperAdmin
                        )
                        Settings -> SettingsScreen(
                            user = user!!,
                            isInDarkTheme = darkModeState.value ?: isSystemInDarkTheme(),
                            onAccountSettingsClick = {
                                navigator.push(
                                    AccountSettings(
                                        token = accessToken,
                                        userStr = user!!.toSerialized()
                                    )
                                )
                            },
                            onAdminToolsClick = {
                                navigator.push(
                                    AdminTools(
                                        token = accessToken,
                                        tagListStr = tagList.map { it.toSerialized() },
                                        regCodesStr = regCodes.map { it.toSerialized() }
                                    )
                                )
                            },
                            onArchiveClick = { },
                            onNotificationsClick = { },
                            onTOSClick = { },
                            onLanguageClick = { },
                            onThemeSwitchClick = { darkModeState.value = it },
                            onSentMessagesClick = {
                                navigator.push(
                                    SentMessages(
                                        accessToken = accessToken,
                                        tagStrList = tagList.map { it.toSerialized() },
                                        userIdentifier = user!!.externalId,
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
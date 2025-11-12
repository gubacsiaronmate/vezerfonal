package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent

@Composable
fun DashboardScreen(content: ComposableContent) {
    val pagerState = rememberPagerState(pageCount = { 5 })
    
    HorizontalPager(state = pagerState) { page ->
        content()
    }
}
package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

data object TagManagement : Screen {
    @Composable
    override fun Content() = Column(Modifier.fillMaxSize()) {  }
}
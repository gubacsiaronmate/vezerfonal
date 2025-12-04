package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.FilterButton
import com.smokinggunstudio.vezerfonal.ui.components.ListItem
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable fun ArchiveScreen() {
    Column(
        modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End) {
            FilterButton(
                onClickEvent = {}
            )
        }
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            repeat(10){
                ListItem(
                title = "example",
                author = "example",
                onClick = {}
            )}
        }
    }
}
package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.ui.components.GroupCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.groups

@Preview(showBackground = true)
@Composable fun GroupScreen(
    groupData: List<GroupData>
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(color = MaterialTheme.colorScheme.surface)) {
        Text(
            text = stringResource(Res.string.groups),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(8.dp)
        )
        Spacer(modifier = Modifier
            .height(16.dp))
        repeat(5) {
            GroupCard(text = "group ${it + 1}")
        }
    }
}
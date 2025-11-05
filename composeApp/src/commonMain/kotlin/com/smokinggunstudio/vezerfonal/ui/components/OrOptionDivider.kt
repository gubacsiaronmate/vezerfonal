package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.or

@Composable fun OrOptionDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1/4F)
                .padding(horizontal = 8.dp),
        )
        Text(
            text = stringResource(Res.string.or),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1/4F)
                .padding(horizontal = 8.dp),
        )
    }
}
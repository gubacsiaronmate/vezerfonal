package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.Event
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.applyStr
import vezerfonal.composeapp.generated.resources.clear

@Composable fun FilterApplyCancelButtons(
    onApply: Event,
    onCancel: Event
) {
    var height by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    
    Row(modifier = Modifier.padding(8.dp)) {
        Button(
            onClick = onCancel,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(
                        topStart = 50F,
                        bottomStart = 50F,
                        topEnd = 0F,
                        bottomEnd = 0F
                    )
                )
                .onGloballyPositioned{ layout ->
                    height = with(density) { layout.size.height.toDp() }
                }
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
            Text(
                text = stringResource(Res.string.clear),
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
        CustomVerticalDivider(Modifier.height(height), color = MaterialTheme.colorScheme.onSurface)
        Button(
            onClick = onApply,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(
                        topStart = 0F,
                        bottomStart = 0F,
                        topEnd = 50F,
                        bottomEnd = 50F
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
            Text(
                text = stringResource(Res.string.applyStr),
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }
}
package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.applyStr
import vezerfonal.composeapp.generated.resources.clear

@Composable fun FilterApplyCancelButtons(
    onApply: CallbackEvent<Boolean>,
    onCancel: ClickEvent
) {
    val height = 32.dp
    val borderColor = MaterialTheme.colorScheme.onSurface
    
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
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(
                        topStart = 50F,
                        bottomStart = 50F,
                        topEnd = 0F,
                        bottomEnd = 0F
                    )
                )
                .height(height)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null
            )
            Text(text = stringResource(Res.string.clear))
        }
        Button(
            onClick = { onApply(false) },
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
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(
                        topStart = 0F,
                        bottomStart = 0F,
                        topEnd = 50F,
                        bottomEnd = 50F
                    )
                )
                .height(height)
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null
            )
            Text(text = stringResource(Res.string.applyStr))
        }
    }
}
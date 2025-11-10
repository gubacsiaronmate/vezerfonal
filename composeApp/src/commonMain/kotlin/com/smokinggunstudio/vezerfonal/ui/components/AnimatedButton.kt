package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent
import kotlinx.coroutines.flow.filterIsInstance


@Composable fun AnimatedButton(
    modifier: Modifier = Modifier,
    content: ComposableContent,
    onClick: ClickEvent,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.interactions
        .filterIsInstance<PressInteraction.Press>()
        .collectAsState(initial = null)

    val scale by animateFloatAsState(targetValue = if (isPressed != null) 0.9f else 1f)

    val elevation by animateDpAsState(targetValue = if (isPressed != null) 4.dp else 10.dp)

    val color = ButtonDefaults.buttonColors(
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary
    )
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
                .scale(scale),
            enabled = true,
            shape = RoundedCornerShape(24.dp),
            colors = color,
            elevation = ButtonDefaults.buttonElevation(elevation),
            contentPadding = PaddingValues(
                horizontal = 80.dp,
                vertical = 12.dp
            ),
            interactionSource = interactionSource
        ) { content() }
    }
}
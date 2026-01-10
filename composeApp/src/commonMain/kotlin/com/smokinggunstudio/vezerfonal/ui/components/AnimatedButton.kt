package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent
import com.smokinggunstudio.vezerfonal.ui.helpers.Event
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Composable fun AnimatedButton(
    onClick: Event,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary
    ),
    elevationPressed: Dp = 4.dp,
    elevationNotPressed: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 80.dp,
        vertical = 12.dp
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: ComposableContent,
) {
    val isPressed by interactionSource.interactions
        .map { interaction ->
            when (interaction) {
                is PressInteraction.Press -> true
                is PressInteraction.Release,
                is PressInteraction.Cancel -> false
                else -> null
            }
        }
        .filterNotNull()
        .distinctUntilChanged()
        .collectAsState(false)
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        label = "scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) elevationPressed else elevationNotPressed,
        label = "elevation"
    )
    
    val elevationOverride = ButtonDefaults.buttonElevation(
        defaultElevation = elevation,
        pressedElevation = elevation,
        focusedElevation = elevation,
        hoveredElevation = elevation,
        disabledElevation = 0.dp
    )
    
    Button(
        onClick = onClick,
        modifier = modifier.scale(scale),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevationOverride,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) { content() }
}
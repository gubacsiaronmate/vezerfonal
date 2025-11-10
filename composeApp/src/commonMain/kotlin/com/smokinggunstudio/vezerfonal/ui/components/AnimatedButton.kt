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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent
import kotlinx.coroutines.flow.filterIsInstance

@Composable fun AnimatedButton(
    content: ComposableContent,
    onClick: ClickEvent,
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
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isPressed by interactionSource.interactions
        .filterIsInstance<PressInteraction.Press>()
        .collectAsState(initial = null)

    val scale by animateFloatAsState(targetValue = if (isPressed != null) 0.9f else 1f)

    val value = if (isPressed == null) elevationNotPressed else elevationPressed
    
    val animation by animateDpAsState(targetValue = value)
    
    val elevation = ButtonDefaults.buttonElevation()
    
    
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) { content() }
    
}
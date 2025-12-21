package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.theme.Black
import kotlin.math.roundToInt

@Composable
fun SlidingConfirmButton(
    roundness: Dp,
    modifier: Modifier = Modifier,
    onComplete: ClickEvent
) {
    BoxWithConstraints(modifier.padding(8.dp)) {
        val height = maxHeight
        val density = LocalDensity.current
        val pxWidth = with(density) { maxWidth.toPx() }
        val knobSize = with(density) { maxHeight.toPx() }
        
        var offset by remember { mutableStateOf(0f) }
        var completed by remember { mutableStateOf(false) }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(roundness)
                )
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(offset.roundToInt(), 0) }
                    .size(height)
                    .background(
                        color = Black,
                        shape = RoundedCornerShape(roundness)
                    )
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (!completed) {
                                    offset = (offset + dragAmount.x)
                                        .coerceIn(0f, pxWidth - knobSize)
                                }
                            },
                            onDragEnd = {
                                if (offset >= pxWidth - knobSize) {
                                    completed = true
                                    onComplete()
                                } else {
                                    offset = 0f
                                }
                            }
                        )
                    }
            )
        }
    }
}
package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Velocity
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MessageTimeRangeSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    initialRange: ClosedFloatingPointRange<Float> = valueRange,
    steps: Int = 0,
    formatLabel: (Float) -> String = { it.toString() },
    onRangeSelected: (ClosedFloatingPointRange<Float>) -> Unit,
    scrollLockedBySliderCallback: CallbackEvent<Boolean>
) {
    val isDragging = remember { mutableStateOf(false) }
    var sliderPosition by remember(initialRange) { mutableStateOf(initialRange) }
    
    if (isDragging.value) scrollLockedBySliderCallback(isDragging.value)
    
    Column(modifier = modifier) {
        RangeSlider(
            value = sliderPosition,
            valueRange = valueRange,
            steps = steps,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = {
                onRangeSelected(sliderPosition)
            },
            modifier = Modifier.pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val ev = awaitPointerEvent()
                        val down = ev.changes.any { it.pressed }
                        if (down) {
                            isDragging.value = true
                        }
                        if (!down && isDragging.value) {
                            isDragging.value = false
                        }
                    }
                }
            }
        )
        Text(text = "From ${formatLabel(sliderPosition.start)} to ${formatLabel(sliderPosition.endInclusive)}")
    }
}
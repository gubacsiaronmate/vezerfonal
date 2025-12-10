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
import com.smokinggunstudio.vezerfonal.helpers.toLocalDateTime
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.end
import vezerfonal.composeapp.generated.resources.start

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MessageTimeRangeSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    initialRange: ClosedFloatingPointRange<Float> = valueRange,
    steps: Int = 0,
    formatLabel: (Float) -> String = { ts: Float ->
        val local = Instant.fromEpochSeconds(ts.toLong()).toLocalDateTime()
        "${local.date.toString().replace("-", ". ")}. ${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
    },
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
        Text(text = "${stringResource(Res.string.start)}: ${formatLabel(sliderPosition.start)}\n${stringResource(Res.string.end)}: ${formatLabel(sliderPosition.endInclusive)}")
    }
}
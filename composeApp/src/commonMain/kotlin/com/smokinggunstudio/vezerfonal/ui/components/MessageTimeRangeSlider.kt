package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.smokinggunstudio.vezerfonal.helpers.toLocalDateTimeDefault
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.end
import vezerfonal.composeapp.generated.resources.start
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
fun MessageTimeRangeSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    initialRange: ClosedFloatingPointRange<Float> = valueRange,
    steps: Int = 0,
    formatLabel: (Float) -> String = { ts: Float ->
        val local = Instant.fromEpochMilliseconds(ts.toLong()).toLocalDateTimeDefault()
        "${local.date.toString().replace("-", ". ")}. ${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
    },
    onRangeSelected: (ClosedFloatingPointRange<Float>) -> Unit,
    scrollLockedBySliderCallback: CallbackFunction<Boolean>
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
package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MessageTimeRangeSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    initialRange: ClosedFloatingPointRange<Float> = valueRange,
    steps: Int = 0,
    formatLabel: (Float) -> String = { it.toString() },
    onRangeSelected: (ClosedFloatingPointRange<Float>) -> Unit = {}
) {
    var sliderPosition by remember { mutableStateOf(initialRange) }
    Column(modifier = modifier) {
        RangeSlider(
            value = sliderPosition,
            valueRange = valueRange,
            steps = steps,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = {
                onRangeSelected(sliderPosition)
            },
        )
        Text(text = "From ${formatLabel(sliderPosition.start)} to ${formatLabel(sliderPosition.endInclusive)}")
    }
}
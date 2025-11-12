package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RangeSlider() {
    var sliderPosition by remember { mutableStateOf(0f..100f) }
    Column {
        RangeSlider(
            value = sliderPosition,
            steps = 5,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..100f,
            onValueChangeFinished = { },
        )
        Text(text = sliderPosition.toString())
    }
}
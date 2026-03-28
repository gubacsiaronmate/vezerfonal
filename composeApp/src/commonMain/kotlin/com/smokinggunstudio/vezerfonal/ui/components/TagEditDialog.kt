package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.applyStr
import vezerfonal.composeapp.generated.resources.cancel
import vezerfonal.composeapp.generated.resources.tag_name

@Composable
fun TagEditDialog(
    onCancelClick: Function,
    modifier: Modifier = Modifier,
    onApplyClick: CallbackFunction<String>,
) {
    var text by remember { mutableStateOf("") }
    
    Dialog(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.tag_name),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Start),
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            OutlinedButton(onCancelClick, shape = MaterialTheme.shapes.extraLarge) { Text(stringResource(Res.string.cancel)) }

            AnimatedButton(onClick = { onApplyClick(text); onCancelClick() }) { Text(stringResource(Res.string.applyStr)) }
        }
    }
}
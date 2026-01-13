package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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

@Composable
fun RegCodeEditDialog(
    onCancelClick: Function,
    modifier: Modifier = Modifier,
    onApplyClick: CallbackFunction<Int?>,
) {
    // TODO: extract string resources
    var text by remember { mutableStateOf("") }
    
    Dialog(
        modifier = modifier
    ) {
        Text("Edit total uses", Modifier.align(Alignment.CenterHorizontally))
        
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onCancelClick) { Text(stringResource(Res.string.cancel)) }
            
            Button({ onApplyClick(text.toIntOrNull()); onCancelClick() }) { Text(stringResource(Res.string.applyStr)) }
        }
    }
}
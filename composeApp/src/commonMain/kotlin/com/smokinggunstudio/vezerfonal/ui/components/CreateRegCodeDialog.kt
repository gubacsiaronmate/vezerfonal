package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.createRegCode
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.genRegCode
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_reg_code
import vezerfonal.composeapp.generated.resources.total_uses

@Composable
fun CreateRegCodeDialog(
    accessToken: String,
    onCancelClick: Function,
    onCreatedRegCode: CallbackFunction<RegCodeData>
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    var totalUses by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    Box(Modifier.fillMaxSize()) {
        CreateDialog(
            titleText = stringResource(Res.string.create_reg_code),
            onCancelClick = onCancelClick,
            onCreateClick = {
                val regCode = RegCodeData(
                    code = genRegCode(),
                    totalUses = totalUses.ifBlank { "1" }.toInt(),
                    remainingUses = totalUses.ifBlank { "1" }.toInt()
                )
                try {
                    scope.launch {
                        if (
                            createRegCode(
                                client = client,
                                accessToken = accessToken,
                                regCode = regCode
                            )
                        ) onCreatedRegCode(regCode)
                        onCancelClick()
                    }
                } catch (e: UnauthorizedException) {
                    error = e
                }
            }
        ) {
            OutlinedTextField(
                value = totalUses,
                onValueChange = { totalUses = it },
                label = { Text(stringResource(Res.string.total_uses)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
        }
        if (error != null) ErrorDialog(error!!)
    }
}
package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.EmailField
import com.smokinggunstudio.vezerfonal.ui.components.PasswordField
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.isValidEmail
import com.smokinggunstudio.vezerfonal.ui.state.controller.RegisterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable fun CredentialsRegisterScreen(
    snapshot: RegisterStateModel,
    onClick: CallbackFunction<RegisterStateModel>
) {
    val state = remember { RegisterStateController(snapshot) }
    
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp),
    ) {
        val confirmPassword = remember { mutableStateOf("") }
        
        RegisterText()
        
        Column(modifier = Modifier.fillMaxWidth()) {
            EmailField(
                value = state.email,
                labelText = stringResource(Res.string.email_address),
                onValueChanged = state::updateEmail
            )
            
            PasswordField(
                value = state.password,
                labelText = stringResource(Res.string.password),
                onValueChanged = state::updatePassword,
                supportingText = stringResource(Res.string.password_must_be_at_least_8_characters_long)
            )
            
            PasswordField(
                value = confirmPassword.value,
                labelText = stringResource(Res.string.confirm_password),
                onValueChanged = { confirmPassword.value = it },
                supportingText = stringResource(Res.string.passwords_must_match)
            )
            
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnimatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 20.dp
                        ),
                    onClick = { onClick(state.snapshot()) },
                    enabled = (
                        state.password == confirmPassword.value
                        && state.password.length >= 8
                        && state.email.isNotBlank()
                        && state.email.isValidEmail()
                        && confirmPassword.value.isNotBlank()
                    ),
                ) { Text(stringResource(Res.string.proceed)) }
            }
        }
    }
}
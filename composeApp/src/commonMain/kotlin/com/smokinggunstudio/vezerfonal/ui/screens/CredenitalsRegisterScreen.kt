package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.EmailField
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.components.PasswordField
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.isValidEmail
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable fun CredentialsRegisterScreen(
    registerState: RegisterState,
    onClick: ClickEvent
) {
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
                value = registerState.email,
                labelText = stringResource(Res.string.email_address),
                onValueChanged = registerState::updateEmail
            )
            
            PasswordField(
                value = registerState.password,
                labelText = stringResource(Res.string.password),
                onValueChanged = registerState::updatePassword
            )
            
            PasswordField(
                value = confirmPassword.value,
                labelText = stringResource(Res.string.confirm_password),
                onValueChanged = { confirmPassword.value = it },
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
                    onClick = onClick,
                    enabled = (
                        registerState.password == confirmPassword.value
                        && registerState.password.length >= 8
                        && registerState.email.isNotBlank()
                        && registerState.email.isValidEmail()
                        && confirmPassword.value.isNotBlank()
                    ),
                ) { Text(stringResource(Res.string.proceed)) }
                
                /*OrOptionDivider()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Column(modifier = Modifier.fillMaxWidth()) {
                    AnimatedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        onClick = {
                            // TODO: Set up OAuth2.0 (DO NOT attempt to connect to backend since no OAuth is set up yet)
                        },
                    ) { Text(stringResource(Res.string.continue_google)) }
                    AnimatedButton(
                        onClick = {
                            // TODO: Set up OAuth2.0 (DO NOT attempt to connect to backend since no OAuth is set up yet)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) { Text(stringResource(Res.string.continue_apple)) }
                }*/
            }
        }
    }
}
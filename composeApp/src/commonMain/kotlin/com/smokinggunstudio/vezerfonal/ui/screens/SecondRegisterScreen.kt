package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState

@Composable fun SecondRegisterScreen(
    registerState: RegisterState,
    onClick: ClickEvent
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp),
    ) {
        val confirmPassword = mutableStateOf("")
        val isPasswConfirmed by remember { mutableStateOf(registerState.password == confirmPassword.value && !confirmPassword.value.isBlank()) }
        
        RegisterText()
        
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = registerState.email,
                onValueChange = registerState::updateEmail,
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            OutlinedTextField(
                value = registerState.password,
                onValueChange = registerState::updatePassword,
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Confirm password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight((1/4F)*3)
            ) {
                Button(
                    onClick = onClick,
                    enabled = isPasswConfirmed,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) { Text("Continue") }
                
                OrOptionDivider()
                
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            // TODO: Set up OAuth2.0 (DO NOT attempt to connect to backend since no OAuth is set up yet)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) { Text("Continue with Google") }
                    Button(
                        onClick = {
                            // TODO: Set up OAuth2.0 (DO NOT attempt to connect to backend since no OAuth is set up yet)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) { Text("Continue with Apple") }
                }
            }
        }
    }
}
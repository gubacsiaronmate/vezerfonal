package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.network.api.loginBasic
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.LoginState
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun LoginScreen(
    client: HttpClient,
    onClick: ClickEvent
) {
    val loginState by mutableStateOf(LoginState())
    val scope = rememberCoroutineScope()
    
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp),
        
    ) {
        Row { Text(
                text = stringResource(Res.string.login),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
        ) }
        
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = loginState.email,
                onValueChange = loginState::updateEmail,
                label = { Text(
                    text = stringResource(Res.string.email_address),
                    color = MaterialTheme.colorScheme.onSurface
                ) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            
            OutlinedTextField(
                value = loginState.password,
                onValueChange = loginState::updatePassword,
                label = { Text(
                    text = stringResource(Res.string.password),
                    color = MaterialTheme.colorScheme.onSurface
                ) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = loginState.rememberMe,
                    onCheckedChange = loginState::updateRememberMe
                )
                
                Text(
                    text = stringResource(Res.string.remember_me),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AnimatedButton(
                    onClick = {
                        scope.launch {
                            loginBasic(loginState, client)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) { Text(
                    text = stringResource(Res.string.login),
                    color = MaterialTheme.colorScheme.onPrimary
                ) }
                
                Text(
                    text = stringResource(Res.string.forgot_password),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .clickable(onClick = onClick)
                )
                
                OrOptionDivider()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AnimatedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) { Text(stringResource(Res.string.continue_google)) }
                
                AnimatedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) { Text(stringResource(Res.string.continue_apple)) }
            }
        }
    }
}
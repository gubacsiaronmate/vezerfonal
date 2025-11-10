package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.smokinggunstudio.vezerfonal.ui.theme.Black
import com.smokinggunstudio.vezerfonal.ui.theme.White
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
        Row {
            Text(
                text = stringResource(Res.string.login),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = loginState.email,
                onValueChange = loginState::updateEmail,
                label = { Text(stringResource(Res.string.email_address),
                    color = MaterialTheme.colorScheme.onSurface) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            OutlinedTextField(
                value = loginState.password,
                onValueChange = loginState::updatePassword,
                label = { Text(stringResource(Res.string.password),
                    color = MaterialTheme.colorScheme.onSurface) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = loginState.rememberMe,
                    onCheckedChange = loginState::updateRememberMe
                )
                Text(text = stringResource(Res.string.remember_me),
                    color = MaterialTheme.colorScheme.onSurface)
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedButton(
                    onClick = {
                        scope.launch {
                            loginBasic(loginState, client)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(Res.string.login),
                        color = MaterialTheme.colorScheme.onPrimary)
                }
                Text(
                    stringResource(Res.string.forgot_password),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 20.dp)
                        .clickable(onClick = onClick)
                )
                OrOptionDivider()
                Spacer(modifier = Modifier.height(24.dp))
                AnimatedButton(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                        .background(color = White)
                ) {
                    Text(stringResource(Res.string.continue_google),
                        color = Black)
                }
                AnimatedButton(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                        .background(color = Black)
                ) {
                    Text(stringResource(Res.string.continue_apple),
                        color = White)
                }
            }
        }
    }
}
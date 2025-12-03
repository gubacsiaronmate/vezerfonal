package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.api.loginBasic
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.state.LoginState
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun LoginScreen(
    client: HttpClient,
    orgs: List<OrgData>,
    onClick: CallbackEvent<TokenResponse>
) {
    val loginState by remember { mutableStateOf(LoginState()) }
    var selectedOrgName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var filtered by remember(orgs) { mutableStateOf(orgs) }
    val isEnabled = remember {( true
        /*loginState.email.isNotBlank()*/ /*&&
        loginState.password.isNotBlank()*/ /*&&
        selectedOrgName.isNotBlank()*/ /*&&
        orgs.any { it.name == selectedOrgName }*/
    )}
    
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
                label = {
                    Text(
                        text = stringResource(Res.string.email_address),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            
            OutlinedTextField(
                value = loginState.password,
                onValueChange = loginState::updatePassword,
                label = {
                    Text(
                        text = stringResource(Res.string.password),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedOrgName,
                    onValueChange = {
                        selectedOrgName = it
                        filtered = orgs
                            .filter { org -> org.name.contains(it, ignoreCase = true) }
                            .sortedWith(
                                compareBy<OrgData> { org -> org.name.removePrefix(it).length }
                                    .thenBy { org -> org.name }
                            )
                    },
                    label = {
                        Text(
                            text = stringResource(Res.string.organization_name),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .onFocusChanged { expanded = it.isFocused },
                )
                
                Box(Modifier.fillMaxWidth()) {
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
                    
                    
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        filtered.forEach { org ->
                            DropdownMenuItem(
                                text = { Text(org.name) },
                                onClick = { selectedOrgName = org.name }
                            )
                        }
                    }
                }
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AnimatedButton(
                    enabled = isEnabled,
                    onClick = {
                        scope.launch {
                            val tokens = loginBasic(
                                loginState = loginState,
                                orgExtId = orgs.find { it.name == selectedOrgName }!!.externalId,
                                client = client
                            )
                            onClick(tokens)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.login),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                
                Text(
                    text = stringResource(Res.string.forgot_password),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .clickable(onClick = { })
                )
                
                /*OrOptionDivider()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AnimatedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) { Text(stringResource(Res.string.continue_google)) }
                
                AnimatedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) { Text(stringResource(Res.string.continue_apple)) }*/
            }
        }
    }
}
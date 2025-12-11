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
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.loginBasic
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.DropdownSearchBar
import com.smokinggunstudio.vezerfonal.ui.components.EmailField
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.components.PasswordField
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
    var selectedOrgExtId by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var counter by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    Box(Modifier.fillMaxSize()) {
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
                    modifier = Modifier.clickable {
                        if (counter < 3) counter++
                        else selectedOrgExtId = "f9b14a894c80421c"
                    }
                )
            }
            
            Column(modifier = Modifier.fillMaxWidth()) {
                EmailField(
                    value = loginState.email,
                    labelText = stringResource(Res.string.email_address),
                    onValueChanged = loginState::updateEmail
                )
                
                PasswordField(
                    value = loginState.password,
                    labelText = stringResource(Res.string.password),
                    onValueChanged = loginState::updatePassword,
                )
                
                Column(Modifier.fillMaxWidth()) {
                    DropdownSearchBar(
                        allItems = orgs,
                        labelText = stringResource(Res.string.organization_name)
                    ) { selectedOrgExtId = it.externalId }
                    
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
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AnimatedButton(
                        enabled = (
                                loginState.email.isNotBlank() &&
                                        loginState.password.isNotBlank() &&
                                        selectedOrgExtId.isNotBlank()
                                ),
                        onClick = {
                            try {
                                scope.launch {
                                    val tokens = loginBasic(
                                        loginState = loginState,
                                        orgExtId = selectedOrgExtId,
                                        client = client
                                    )
                                    onClick(tokens)
                                }
                            } catch (e: UnauthorizedException) {
                                error = e
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
        if (error != null) ErrorDialog(error!!.message!!, true)
    }
}
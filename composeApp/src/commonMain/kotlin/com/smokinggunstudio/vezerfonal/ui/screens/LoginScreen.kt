package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.network.api.loginBasic
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.DropdownSearchBar
import com.smokinggunstudio.vezerfonal.ui.components.EmailField
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.PasswordField
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.state.controller.LoginStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.LoginStateModel
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun LoginScreen(
    client: HttpClient,
    orgsStr: List<String>,
    onClick: CallbackFunction<TokenResponse>
) {
    val orgs = orgsStr.map { it.toDTO<OrgData>() }
    val state by remember { mutableStateOf(LoginStateController(LoginStateModel())) }
    var selectedOrgExtId by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var counter by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val ourTestOrgExtId = "f9b14a894c80421c"
    
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
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        if (counter <= 3) counter++
                        else selectedOrgExtId = ourTestOrgExtId
                    }
                )
            }
            
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
                )
                
                Column(Modifier.fillMaxWidth()) {
                    DropdownSearchBar(
                        allItems = orgs,
                        labelText = stringResource(Res.string.organization_name)
                    ) { selectedOrgExtId = it.externalId }
                    
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.rememberMe,
                            onCheckedChange = state::updateRememberMe
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
                            state.email.isNotBlank() &&
                            state.password.isNotBlank() &&
                            selectedOrgExtId.isNotBlank()
                        ),
                        onClick = {
                            scope.launch {
                                try {
                                    val tokens = loginBasic(
                                        loginState = state.snapshot(),
                                        orgExtId = selectedOrgExtId,
                                        client = client
                                    )
                                    onClick(tokens)
                                } catch (e: Exception) {
                                    error = e
                                }
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
                }
            }
        }
        if (error != null) ErrorDialog(error!!, Modifier.align(Alignment.Center))
    }
}
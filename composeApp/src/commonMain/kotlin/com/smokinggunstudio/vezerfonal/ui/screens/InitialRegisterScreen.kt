package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.navigation.CreateOrg
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.NonAdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_organization
import vezerfonal.composeapp.generated.resources.proceed
import vezerfonal.composeapp.generated.resources.registration_code

@Composable fun InitialRegisterScreen(
    onContinueClick: ClickEvent,
    onCreateOrgClick: ClickEvent,
    returnRegState: CallbackEvent<RegisterState>,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp),
    ) {
        val nonAdminRegisterState = rememberSaveable(saver = NonAdminRegisterState.Saver) { NonAdminRegisterState() }
        
        RegisterText()
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1/2F),
                verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            OutlinedTextField(
                value = nonAdminRegisterState.regCode,
                onValueChange = nonAdminRegisterState::updateRegCode,
                label = { Text(stringResource(Res.string.registration_code), color = MaterialTheme.colorScheme.onSurface) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnimatedButton(
                    onClick = {
                        returnRegState(nonAdminRegisterState)
                        onContinueClick()
                    },
                    enabled = nonAdminRegisterState.regCode.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.proceed),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                OrOptionDivider()
                
                Spacer(modifier = Modifier.height(16.dp))
                
                AnimatedButton(
                    onClick = {
                        returnRegState(AdminRegisterState())
                        onCreateOrgClick()
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.create_organization),
                    color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
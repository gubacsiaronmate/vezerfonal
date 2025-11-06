package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.NonAdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_organization
import vezerfonal.composeapp.generated.resources.proceed
import vezerfonal.composeapp.generated.resources.registration_code

@Composable fun FirstRegisterScreen(
    onClickCallback: CallbackClickEvent<RegisterState>,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                label = { Text(stringResource(Res.string.registration_code)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onClickCallback(nonAdminRegisterState)
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) { Text(stringResource(Res.string.proceed)) }
                Spacer(modifier = Modifier.height(16.dp))
                OrOptionDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onClickCallback(AdminRegisterState())
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) { Text(stringResource(Res.string.create_organization)) }
            }
        }
    }
}
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
import com.smokinggunstudio.vezerfonal.helpers.CallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.components.OrOptionDivider
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.state.AdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.NonAdminRegisterState
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState

@Composable fun FirstRegisterScreen(
    onClickCallbackNonAdmin: CallbackClickEvent<RegisterState>,
    onClickCallbackAdmin: CallbackClickEvent<RegisterState>
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
                .fillMaxHeight(1/2F)
        ) {
            OutlinedTextField(
                value = nonAdminRegisterState.regCode,
                onValueChange = { nonAdminRegisterState.updateRegCode(it) },
                label = { Text("Registration Code") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )
            
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight((1/4F)*3)
            ) {
                Button(
                    onClick = {
                        onClickCallbackNonAdmin(nonAdminRegisterState)
                    },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) { Text("Continue") }
                
                OrOptionDivider()
                
                Button(
                    onClick = {
                        onClickCallbackAdmin(AdminRegisterState())
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) { Text("Create Organisation") }
            }
        }
    }
}
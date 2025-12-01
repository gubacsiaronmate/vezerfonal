package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.state.CreateOrganizationState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create
import vezerfonal.composeapp.generated.resources.create_organization
import vezerfonal.composeapp.generated.resources.organization_name

@Preview
@Composable
fun CreateOrganizationScreen() {
    val state = remember { CreateOrganizationState() }
    
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.create_organization),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.displaySmall
        )
        OutlinedTextField(
            value = state.organizationName,
            onValueChange = state::updateOrganizationName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
            label = {Text(
                text = stringResource(Res.string.organization_name),
                color = MaterialTheme.colorScheme.onSurface
            )}
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            
            onClick = {}
            ) {
            Text(text = stringResource(Res.string.create))
        }
    }
}
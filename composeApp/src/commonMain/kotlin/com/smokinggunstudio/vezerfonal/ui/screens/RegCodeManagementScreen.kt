package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.network.api.createRegCode
import com.smokinggunstudio.vezerfonal.ui.components.CreateRegCodeDialog
import com.smokinggunstudio.vezerfonal.ui.components.SwipeableRegCodeCard
import com.smokinggunstudio.vezerfonal.ui.helpers.genRegCode
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_reg_code
import vezerfonal.composeapp.generated.resources.registration_code_management

@Composable
fun RegCodeManagementScreen(
    client: HttpClient,
    accessToken: String,
    registrationCodes: List<RegCodeData>
) {
    val scope = rememberCoroutineScope()
    var regCodes by remember(registrationCodes) { mutableStateOf(registrationCodes) }
    var isCreateRegCodeOpened by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.registration_code_management),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = { isCreateRegCodeOpened = true },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Add, null)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(Res.string.create_reg_code),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
            
            regCodes.forEach { code ->
                SwipeableRegCodeCard(
                    onDelete = {
                        regCodes = regCodes.filter { it != code }
                        scope.launch {
                            // TODO: Add logic to delete reg code
                        }
                    },
                    regCode = code
                )
            }
        }
        
        if (isCreateRegCodeOpened)
            CreateRegCodeDialog(
                client = client,
                accessToken = accessToken,
                onCancelClick = { isCreateRegCodeOpened = false }
            ) { regCodes += it }
    }
}
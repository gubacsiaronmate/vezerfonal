package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.network.api.deleteRegCode
import com.smokinggunstudio.vezerfonal.network.api.patchCode
import com.smokinggunstudio.vezerfonal.ui.components.CreateRegCodeDialog
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.RegCodeEditDialog
import com.smokinggunstudio.vezerfonal.ui.components.SwipeableRegCodeCard
import io.ktor.client.*
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
    var selectedCode by remember { mutableStateOf("") }
    var isRegCodeEditOpened by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
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
            
            regCodes.forEach { code: RegCodeData ->
                SwipeableRegCodeCard(
                    onDelete = {
                        regCodes = regCodes.filter { it != code }
                        scope.launch {
                            deleteRegCode(code.code, client, accessToken)
                        }
                    },
                    onEdit = {
                        isRegCodeEditOpened = true
                        selectedCode = code.code
                    },
                    regCode = code
                )
            }
        }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            if (isCreateRegCodeOpened)
                CreateRegCodeDialog(
                    client = client,
                    accessToken = accessToken,
                    onCancelClick = { isCreateRegCodeOpened = false }
                ) { regCodes += it }
            
            if (isRegCodeEditOpened && selectedCode.isNotBlank())
                RegCodeEditDialog({ isRegCodeEditOpened = false }) { newTotalUses ->
                    if (newTotalUses == null) return@RegCodeEditDialog
                    
                    val newCode = RegCodeData(
                        code = selectedCode,
                        totalUses = newTotalUses,
                        remainingUses = newTotalUses
                    )
                    
                    regCodes = regCodes.map { if (it.code != selectedCode) it else newCode }
                    
                    scope.launch {
                        try {
                            patchCode(
                                client = client,
                                accessToken = accessToken,
                                regCode = newCode,
                            )
                        } catch (e: Exception) {
                            error = e
                        }
                    }
                }
            
            if (error != null) ErrorDialog(error!!)
        }
    }
}
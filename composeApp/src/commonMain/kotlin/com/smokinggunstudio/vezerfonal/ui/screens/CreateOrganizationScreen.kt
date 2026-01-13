package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.helpers.NotCreatedException
import com.smokinggunstudio.vezerfonal.helpers.getExtId
import com.smokinggunstudio.vezerfonal.network.api.createOrgRequest
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.state.controller.RegisterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create
import vezerfonal.composeapp.generated.resources.create_organization
import vezerfonal.composeapp.generated.resources.organization_name

@Composable
fun CreateOrganizationScreen(
    snapshot: RegisterStateModel,
    client: HttpClient,
    onClick: CallbackFunction<RegisterStateModel>
) {
    val scope = rememberCoroutineScope()
    val state = remember { RegisterStateController(snapshot) }
    var error by remember { mutableStateOf<Throwable?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(Res.string.create_organization),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displaySmall
            )
            OutlinedTextField(
                value = state.extra,
                onValueChange = state::updateExtra,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp,
                    ),
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(Res.string.organization_name),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    try {
                        scope.launch {
                            if (createOrgRequest(
                                org = OrgData(
                                    name = state.extra,
                                    externalId = getExtId()
                                ),
                                client = client
                            )) onClick(state.apply { setIsAdmin(true) }.snapshot())
                        }
                    } catch (e: NotCreatedException) { error = e }
                }
            ) { Text(text = stringResource(Res.string.create)) }
        }

        if (error != null) ErrorDialog(error!!)
    }
}
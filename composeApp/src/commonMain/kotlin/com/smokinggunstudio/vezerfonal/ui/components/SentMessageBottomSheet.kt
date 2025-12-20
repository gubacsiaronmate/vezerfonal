package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.network.api.getUsersByIdentifierList
import io.ktor.client.HttpClient
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SentMessageBottomSheet(
    accessToken: String,
    reactions: List<String>
) {
    val client = LocalHttpClient.current
    var users by remember { mutableStateOf<List<UserData>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    val identifierList = reactions.map { it.toDTO<InteractionInfoData>().userIdentifier }
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    LaunchedEffect(Unit) {
        loading = true
        val d = try {
            getUsersByIdentifierList(
                identifiers = identifierList,
                accessToken = accessToken,
                client = client,
            )
        } catch (e: Exception) { error = e; emptyList() }
        users = d
        loading = false
    }
    
    if (reactions.isNotEmpty())
        ModalBottomSheet(
            onDismissRequest = { },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box {
                if (loading) LinearProgressIndicator()
                else if (!loading && error != null)
                    ErrorDialog(
                        errorMessage = error!!.message!!,
                        isUnauthed = error is UnauthorizedException,
                        modifier = Modifier.align(Alignment.Center)
                    )
                else Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    reactions.forEach { interaction ->
                        val reaction = interaction.toDTO<InteractionInfoData>()
                        val username = users.single { it.identifier == reaction.userIdentifier }.name
                        SentMsgBottomSheetRow(reaction.reaction!!, username)
                    }
                }
            }
        }
}
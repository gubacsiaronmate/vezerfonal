package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.toUrlValidFormat
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.reveal_email
import vezerfonal.composeapp.generated.resources.reveal_id

@Composable
fun AccountSettingsNameCard(user: UserData) {
    var revealEmail by remember { mutableStateOf(false) }
    var revealId by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfilePicture(
                name = user.name.toUrlValidFormat(),
                size = 100.dp,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            Column(Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier.padding(4.dp)
                    )
                    IconButton({
                    
                    }) {
                        Image(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
                Row(Modifier.clickable { revealId = !revealId }) {
                    Image(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text =
                            if (revealId) user.externalId
                            else stringResource(Res.string.reveal_id),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Row(Modifier.clickable { revealEmail = !revealEmail }) {
                    Image(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                    )
                    Text(
                        text =
                            if (revealEmail) user.email
                            else stringResource(Res.string.reveal_email),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}
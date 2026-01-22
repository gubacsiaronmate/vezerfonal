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
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.network.api.deleteRegCode
import com.smokinggunstudio.vezerfonal.network.api.patchCode
import com.smokinggunstudio.vezerfonal.network.api.tagDelete
import com.smokinggunstudio.vezerfonal.network.api.tagPut
import com.smokinggunstudio.vezerfonal.ui.components.CreateRegCodeDialog
import com.smokinggunstudio.vezerfonal.ui.components.CreateTagDialog
import com.smokinggunstudio.vezerfonal.ui.components.ErrorDialog
import com.smokinggunstudio.vezerfonal.ui.components.RegCodeEditDialog
import com.smokinggunstudio.vezerfonal.ui.components.SwipeableRegCodeCard
import com.smokinggunstudio.vezerfonal.ui.components.SwipeableTagCard
import com.smokinggunstudio.vezerfonal.ui.components.TagEditDialog
import com.smokinggunstudio.vezerfonal.ui.helpers.HomeCache.regCodes
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_reg_code
import vezerfonal.composeapp.generated.resources.create_tag
import vezerfonal.composeapp.generated.resources.registration_code_management
import vezerfonal.composeapp.generated.resources.tag_management

@Composable
fun TagManagementScreen(
    accessToken: String,
    tagsList: List<TagData>
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    var tags by remember(tagsList) { mutableStateOf(tagsList) }
    var isCreateTagOpened by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf("") }
    var isTagEditOpened by remember { mutableStateOf(false) }
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
                    text = stringResource(Res.string.tag_management),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = { isCreateTagOpened = true },
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
                            text = stringResource(Res.string.create_tag),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
            
            tags.forEach { tag: TagData ->
                SwipeableTagCard(
                    onDelete = {
                        tags = tags.filter { it != tags }
                        scope.launch {
                            tagDelete(client, accessToken, tag)
                        }
                    },
                    onEdit = {
                        isTagEditOpened = true
                        selectedTag = tag.name
                    },
                    tag = tag
                )
            }
        }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            if (isCreateTagOpened)
                CreateTagDialog(
                    accessToken = accessToken,
                    onCancelClick = { isCreateTagOpened = false }
                ) { tags += it }
            
            if (isTagEditOpened && selectedTag.isNotBlank())
                TagEditDialog({ isTagEditOpened = false }) { newTagName ->
                    val newTag = TagData(newTagName)
                    
                    tags = tags.map { if (it.name != selectedTag) it else newTag }
                    
                    scope.launch {
                        try {
                            tagPut(client, accessToken, newTag)
                        } catch (e: Exception) {
                            error = e
                        }
                    }
                }
            
            if (error != null) ErrorDialog(error!!)
        }
    }
}
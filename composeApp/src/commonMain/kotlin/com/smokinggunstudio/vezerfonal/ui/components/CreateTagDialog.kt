package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.network.api.tagCreate
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.create_tag
import vezerfonal.composeapp.generated.resources.tag_name

@Composable
fun CreateTagDialog(
    accessToken: String,
    onCancelClick: Function,
    onCreatedTag: CallbackFunction<TagData>
) {
    val client = LocalHttpClient.current
    val scope = rememberCoroutineScope()
    var tagName by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    Box(Modifier.fillMaxSize()) {
        CreateDialog(
            titleText = stringResource(Res.string.create_tag),
            onCancelClick = onCancelClick,
            onCreateClick = {
                try {
                    scope.launch {
                        val newTag = TagData(tagName)
                        tagCreate(client, accessToken, newTag)
                        onCreatedTag(newTag)
                        onCancelClick()
                    }
                } catch (e: Exception) {
                    error = e
                }
            }
        ) {
            OutlinedTextField(
                value = tagName,
                onValueChange = { tagName = it },
                label = { Text(stringResource(Res.string.tag_name)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
        }
        if (error != null) ErrorDialog(error!!)
    }
}
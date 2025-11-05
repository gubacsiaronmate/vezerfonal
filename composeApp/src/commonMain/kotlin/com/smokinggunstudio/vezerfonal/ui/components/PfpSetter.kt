package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.FilePicker
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackClickEvent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.set_profile_picture

@Composable fun PfpSetter(
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    modifier: Modifier = Modifier,
    onFilePickCallBack: CallbackClickEvent<FileData?>
) {
    val filePicker = FilePicker()
    val scope = rememberCoroutineScope()
    
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.set_profile_picture),
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(
            onClick = {
                var data: FileData? = null
                scope.launch { data = filePicker.pickFile() }
                onFilePickCallBack(data)
            },
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(500.dp)
                )
        ) { Image(
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
        ) }
    }
}
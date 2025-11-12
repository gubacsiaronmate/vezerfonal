package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.FilePicker
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.toImageResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.set_profile_picture

@Composable fun PfpSetter(
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier,
    onFilePickCallBack: CallbackClickEvent<FileData?>
) {
    val filePicker = FilePicker()
    val scope = rememberCoroutineScope()
    var data: FileData? by remember { mutableStateOf(null) }
    
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
            shape = CircleShape,
            onClick = {
                val job = scope.launch { data = filePicker.pickFile() }
                if ((job.start() && (job.isCompleted || job.isCancelled)) || data != null) onFilePickCallBack(data)
            },
            modifier = modifier
                .height(120.dp)
                .width(120.dp)
                .aspectRatio(1F)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                ),
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) { Row {
                data?.let {
                    Image(
                        bitmap = it.toImageResource(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                
                if (data == null) Image(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                )
            } }
        }
    }
}
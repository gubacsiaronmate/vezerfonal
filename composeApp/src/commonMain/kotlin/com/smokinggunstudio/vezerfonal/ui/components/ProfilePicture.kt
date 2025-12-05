package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.ui.helpers.toImageResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable fun ProfilePicture(
    size: Dp = 120.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier
) {
    var data: FileData? by remember { mutableStateOf(null) }
    
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        IconButton(
            shape = CircleShape,
            onClick = {},
            modifier = modifier
                .height(size)
                .width(size)
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
                } ?: Image(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                )
            } }
        }
    }
}
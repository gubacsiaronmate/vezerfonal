package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.network.api.getProfilePicture
import com.smokinggunstudio.vezerfonal.ui.helpers.svgXMLToByteArray
import com.smokinggunstudio.vezerfonal.ui.helpers.toImageResource
import kotlin.math.roundToInt

@Composable
fun ProfilePicture(
    name: String = "",
    size: Dp = 120.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier
) {
    var data: FileData? by remember { mutableStateOf(null) }
    var loading by remember { mutableStateOf(false) }
    val pxSize = with(LocalDensity.current) { size.toPx() }.roundToInt()
    
    LaunchedEffect(Unit) {
        loading = true
        val d = try {
            getProfilePicture(name, pxSize)
        } catch (e: Exception) {
            log { "${e.message}\n${e.printStackTrace()}" }
            null
        }
        data = d
        loading = false
    }
    
    
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
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                if (loading) CircularProgressIndicator()
                else Row {
                    data?.let {
                        if (it.metaData.mimeType == "image/svg+xml")
                            Image(
                                bitmap = it.svgXMLToByteArray(pxSize),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )
                        else Image(
                            bitmap = it.toImageResource(),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: Image(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}
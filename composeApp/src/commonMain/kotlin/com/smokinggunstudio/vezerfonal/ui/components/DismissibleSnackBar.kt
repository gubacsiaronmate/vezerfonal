package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.*
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent

@Composable fun DismissibleSnackBar(visibility: Boolean = true, content: ComposableContent) {
    var visible by remember { mutableStateOf(visibility) }
    
    if (visible) Snackbar(
        content = content,
        dismissAction = { IconButton({ visible = false }) {
            Image(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        } }
    )
}
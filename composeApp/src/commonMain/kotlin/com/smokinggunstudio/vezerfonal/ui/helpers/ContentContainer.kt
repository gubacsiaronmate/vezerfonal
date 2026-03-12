package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Centers content horizontally and caps its width to 840dp on Expanded screens,
 * preventing layouts from stretching unreadably on wide desktop/web viewports.
 */
@Composable
fun ContentContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val windowSize = LocalWindowSizeInfo.current
    val isExpanded = windowSize.widthClass == WindowWidthClass.Expanded
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (isExpanded) Modifier.widthIn(max = 840.dp) else Modifier),
        contentAlignment = Alignment.TopCenter,
        content = content,
    )
}

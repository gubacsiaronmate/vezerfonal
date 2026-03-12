package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.filter

@Composable fun FilterButton(
    onClickFunction: Function,
    activeFilterCount: Int = 0,
) {
    FilterChip(
        selected = activeFilterCount > 0,
        onClick = onClickFunction,
        label = {
            val label = stringResource(Res.string.filter)
            Text(if (activeFilterCount > 0) "$label ($activeFilterCount)" else label)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.FilterList, contentDescription = null)
        },
    )
}

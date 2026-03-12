package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthClass {
    /** < 600dp — phone portrait */
    Compact,
    /** 600–839dp — tablet portrait / phone landscape */
    Medium,
    /** ≥ 840dp — desktop / tablet landscape / wide browser */
    Expanded,
}

data class WindowSizeInfo(
    val widthClass: WindowWidthClass,
    val widthDp: Dp,
)

fun windowWidthClassFor(widthDp: Dp): WindowWidthClass = when {
    widthDp < 600.dp -> WindowWidthClass.Compact
    widthDp < 840.dp -> WindowWidthClass.Medium
    else             -> WindowWidthClass.Expanded
}

val LocalWindowSizeInfo = staticCompositionLocalOf {
    WindowSizeInfo(WindowWidthClass.Compact, 360.dp)
}

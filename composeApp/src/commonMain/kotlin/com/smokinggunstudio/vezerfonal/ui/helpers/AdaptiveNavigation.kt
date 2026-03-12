package com.smokinggunstudio.vezerfonal.ui.helpers

enum class NavType {
    /** Bottom navigation bar — phone/Compact */
    BottomBar,
    /** Side navigation rail — tablet/Medium */
    Rail,
    /** Permanent navigation drawer — desktop/Expanded */
    Drawer,
}

fun WindowWidthClass.navType(): NavType = when (this) {
    WindowWidthClass.Compact  -> NavType.BottomBar
    WindowWidthClass.Medium   -> NavType.Rail
    WindowWidthClass.Expanded -> NavType.Drawer
}

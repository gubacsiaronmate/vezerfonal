package com.smokinggunstudio.vezerfonal.network.helpers

actual class Platform {
    actual companion object {
        actual val type: PlatformType
        get() = PlatformType.Desktop
    }
}
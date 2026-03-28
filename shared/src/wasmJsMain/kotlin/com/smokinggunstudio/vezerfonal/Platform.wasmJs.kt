package com.smokinggunstudio.vezerfonal

class WasmJsPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmJsPlatform()

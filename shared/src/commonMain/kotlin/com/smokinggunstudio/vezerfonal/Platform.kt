package com.smokinggunstudio.vezerfonal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
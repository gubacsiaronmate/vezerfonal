package com.smokinggunstudio.vezerfonal

class Greeting {
    private val platform = getPlatform()
    
    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
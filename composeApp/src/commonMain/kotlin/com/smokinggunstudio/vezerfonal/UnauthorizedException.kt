package com.smokinggunstudio.vezerfonal

import com.smokinggunstudio.vezerfonal.ui.navigation.ViewMessage

class UnauthorizedException(override val message: String?) : Exception(message) {
    constructor() : this("Unauthorized.")
}
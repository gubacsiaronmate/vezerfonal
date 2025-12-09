package com.smokinggunstudio.vezerfonal.helpers

class UnauthorizedException(override val message: String?) : Exception(message) {
    constructor() : this("Unauthorized.")
}
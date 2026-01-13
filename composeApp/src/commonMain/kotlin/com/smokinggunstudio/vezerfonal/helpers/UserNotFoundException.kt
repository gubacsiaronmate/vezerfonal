package com.smokinggunstudio.vezerfonal.helpers

class UserNotFoundException(override val message: String?) : Exception(message) {
    constructor() : this("User not found.")
}
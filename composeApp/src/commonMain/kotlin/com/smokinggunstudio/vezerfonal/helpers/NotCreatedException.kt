package com.smokinggunstudio.vezerfonal.helpers

class NotCreatedException(override val message: String?) : Exception(message) {
    constructor() : this("Not created.")
}
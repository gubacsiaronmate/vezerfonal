package com.smokinggunstudio.vezerfonal.helpers

class UnableToLoadException(override val message: String?) : Exception(message) {
    constructor() : this("Unable to load data.")
}
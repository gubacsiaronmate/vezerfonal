package com.smokinggunstudio.vezerfonal.helpers

import io.ktor.http.HttpStatusCode

class UnableToLoadException(override val message: String?) : Exception(message) {
    constructor() : this("Unable to load data.")
    constructor(statusCode: HttpStatusCode) : this("Unable to load data. Status code: $statusCode")
}
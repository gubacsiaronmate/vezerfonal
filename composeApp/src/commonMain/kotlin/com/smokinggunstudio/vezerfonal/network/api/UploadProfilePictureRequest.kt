package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

suspend fun uploadProfilePicture(
    client: HttpClient,
    accessToken: String,
    fileData: FileData,
): String {
    val response = client.post(NetworkConstants.Endpoints.UPLOAD_PFP) {
        bearerAuth(accessToken)
        contentType(ContentType.parse(fileData.metaData.mimeType))
        setBody(fileData.bytes)
    }
    return when (val status = response.status) {
        HttpStatusCode.OK -> response.body<String>()
        HttpStatusCode.Unauthorized -> throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}

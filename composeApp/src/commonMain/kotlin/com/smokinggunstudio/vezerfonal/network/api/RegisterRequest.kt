package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.network.helpers.Platform
import com.smokinggunstudio.vezerfonal.network.helpers.PlatformType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.*

suspend fun registerBasic(
    userData: UserData,
    rememberMe: Boolean,
    fileData: FileData,
    client: HttpClient
): TokenResponse {
    val (id: Int, success: Boolean) = (
        client.post(
            NetworkConstants.Endpoints.REGISTER_DATA_BASIC
        ) { setBody(userData) }
    ).let {
        Pair(
            it.body<String>().trim().toInt(),
            it.status == HttpStatusCode.Created
        )
    }
    
    if (!success) error("Could not register.")
    
    val baseURL = NetworkConstants.Endpoints.REGISTER_PICTURE + id +
        when (Platform.type) {
            PlatformType.Desktop -> "/false"
            PlatformType.JS -> "/false"
            else -> "/$rememberMe"
        }
    
    val urlMetadata = "$baseURL/metadata"
    
    val acceptance = (client.post(urlMetadata) { setBody(fileData.metaData) }).status == HttpStatusCode.Accepted
    
    if (!acceptance) error("Metadata didn't get received.")
    
    val urlData = "$baseURL/filedata"
    
    val response = client.submitFormWithBinaryData(
        url = urlData,
        formData = formData {
            append(
                key = "file",
                value = fileData.bytes,
                headers = headers {
                    append(HttpHeaders.ContentType, fileData.metaData.mimeType)
                    append(
                        HttpHeaders.ContentDisposition,
                        """form-data; name="file"; filename="${fileData.metaData.name}"""
                    )
                }
            )
        }
    )
    
    val tokens = response.body<TokenResponse>()
    return tokens
}
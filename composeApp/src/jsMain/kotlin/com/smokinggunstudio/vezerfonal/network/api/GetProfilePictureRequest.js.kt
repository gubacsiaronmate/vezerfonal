package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.network.client.provideEngine
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.ui.helpers.toFileData
import com.smokinggunstudio.vezerfonal.ui.helpers.toUrlValidFormat
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

actual suspend fun getProfilePicture(name: String, pxSize: Int): FileData {
    val client = HttpClient(provideEngine())
    val safeName = name.toUrlValidFormat()
    val url = NetworkConstants.PFP_URL + safeName + "&size=$pxSize"
    val svgXML = client.get(url).bodyAsText()
    return svgXML.encodeToByteArray().toFileData("${safeName}_profile_picture.svg")
}

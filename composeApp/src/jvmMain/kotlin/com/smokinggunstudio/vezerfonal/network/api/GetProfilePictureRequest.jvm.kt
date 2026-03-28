package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.network.client.provideEngine
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.ui.helpers.toFileData
import com.smokinggunstudio.vezerfonal.ui.helpers.toUrlValidFormat
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun getProfilePicture(name: String, pxSize: Int): FileData {
    val client = HttpClient(provideEngine())
    val safeName = name.toUrlValidFormat()
    val url = NetworkConstants.PFP_URL + safeName + "&size=$pxSize"
    val fileName = "${safeName}_profile_picture.svg"
    val file = File(System.getProperty("java.io.tmpdir"), fileName)
    if (file.exists() && file.length() > 0L) {
        val cached = file.readBytes()
        if (cached.decodeToString().trimStart().startsWith("<svg", ignoreCase = true)) {
            return cached.toFileData(fileName)
        }
        file.delete()
    }
    val svgXML = client.get(url).bodyAsText()
    if (!svgXML.trimStart().startsWith("<svg", ignoreCase = true) &&
        !svgXML.trimStart().startsWith("<?xml", ignoreCase = true)) {
        error("getProfilePicture: server returned non-SVG content: ${svgXML.take(120)}")
    }
    val bytes = svgXML.encodeToByteArray()
    withContext(Dispatchers.IO) { file.writeBytes(bytes) }
    return bytes.toFileData(fileName)
}

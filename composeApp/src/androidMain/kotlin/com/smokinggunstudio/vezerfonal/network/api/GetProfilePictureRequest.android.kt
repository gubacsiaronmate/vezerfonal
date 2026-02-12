package com.smokinggunstudio.vezerfonal.network.api

import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.FileMetaData
import com.smokinggunstudio.vezerfonal.helpers.security.CurrentContextProvider
import com.smokinggunstudio.vezerfonal.network.client.createHttpClient
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.ui.helpers.svgXMLToByteArray
import com.smokinggunstudio.vezerfonal.ui.helpers.toFileData
import com.smokinggunstudio.vezerfonal.ui.helpers.toImageResource
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun getProfilePicture(name: String, size: Int): FileData? {
    val client = createHttpClient(false)
    val url = NetworkConstants.PFP_URL + name
    val context = CurrentContextProvider.current
    val fileName = "${name}_profile_picture.jpeg"
    val file = context?.filesDir?.let { File(it, fileName) }
    return if (context != null && file != null && file.exists()) {
        val bytes = file.readBytes()
        val fileData = bytes.toFileData(fileName)
        fileData
    } else {
        val response = client.get(url)
        val svgXML = response.body<String>()
        val bytes = svgXML.svgXMLToByteArray(size)
        if (context != null) {
            if (file != null) {
                if (!file.exists())
                    withContext(Dispatchers.IO) { file.createNewFile() }
                
                file.writeBytes(bytes)
            }
        }
        val fileData = bytes.toFileData(fileName)
        fileData
    }
}
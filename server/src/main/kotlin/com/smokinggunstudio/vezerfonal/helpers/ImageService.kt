package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.util.unaryPlus
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext

class ImageService(
    private val pfpDir: String
) {
    
    /**
     * Saves an uploaded image file and returns the filename
     * @param multipart The multipart data containing the image
     * @param userId The user ID for organizing files (optional)
     * @return The saved filename or null if failed
     */
    suspend fun saveImage(
        multipart: MultiPartData,
        userId: Int,
        context: CoroutineContext
    ): String = withContext(context) {
        var savedFileName = "nofile"
        
        multipart.forEachPart { part ->
            if (part !is PartData.FileItem) error("Part is not FileItem type.")
            val fileBytes = part.provider().readBuffer().readByteArray()
            val extension = part.originalFileName?.substringAfterLast(".", "jpg")
            savedFileName = buildString {
                + "user_${userId}_"
                + UUID.randomUUID().toString()
                + ".$extension"
            }
            val file = File("$pfpDir/$savedFileName")
            file.writeBytes(fileBytes)
            part.dispose()
        }
        "$pfpDir/$savedFileName"
    }
    
    /**
     * Saves a raw byte array as an image
     * @param bytes The image bytes
     * @param extension File extension (default: jpg)
     * @param userId The user ID for organizing files
     * @return The saved filename
     */
    suspend fun saveImageBytes(
        bytes: ByteArray,
        userId: Int,
        context: CoroutineContext,
        extension: String = "jpg"
    ): String = withContext(context) {
        val savedFileName = buildString {
            + "user_${userId}_"
            + UUID.randomUUID().toString()
            + ".$extension"
        }
        
        val file = File("$pfpDir/$savedFileName")
        file.writeBytes(bytes)
        "$pfpDir/$savedFileName"
    }
    
    fun getImage(filename: String): File? =
        File(pfpDir, filename).takeIf { it.exists() && it.isFile }
    
    fun getImageResponse(filename: String): ImageResponse? {
        val file = getImage(filename)
        return if (file != null) {
            ImageResponse(
                file = file,
                fileType = when (file.name.substringAfterLast(".", "").lowercase()) {
                    "jpg", "jpeg" -> ContentType.Image.JPEG
                    "png" -> ContentType.Image.PNG
                    "gif" -> ContentType.Image.GIF
                    "webp" -> ContentType.Image.WEBP
                    else -> throw IllegalArgumentException("Unsupported image format: ${file.name}")
                }
            )
        } else null
    }
}
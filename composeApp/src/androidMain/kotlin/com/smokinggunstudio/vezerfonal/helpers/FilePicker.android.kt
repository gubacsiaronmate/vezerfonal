package com.smokinggunstudio.vezerfonal.helpers

import android.provider.OpenableColumns
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

actual class FilePicker {
    actual suspend fun pickFile(): FileData? = withContext(Dispatchers.Main) {
        val activity = CurrentActivityProvider.current ?: return@withContext null
        suspendCancellableCoroutine { cont ->
            val registry = activity.activityResultRegistry
            val key = "file-picker-" + System.currentTimeMillis()
            lateinit var launcher: ActivityResultLauncher<Array<String>>
            launcher = registry.register(key, activity, ActivityResultContracts.OpenDocument()) { uri ->
                try {
                    if (uri == null) {
                        if (cont.isActive) cont.resume(null)
                        return@register
                    }
                    val cr = activity.contentResolver
                    val name = cr.query(uri, null, null, null, null)?.use { cursor ->
                        val nameIndex: Int = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex >= 0 && cursor.moveToFirst()) cursor.getString(nameIndex) else null
                    } ?: uri.lastPathSegment ?: "file"
                    val mime = cr.getType(uri) ?: "application/octet-stream"
                    val bytes = cr.openInputStream(uri)?.use { it.readBytes() } ?: ByteArray(0)
                    if (cont.isActive) cont.resume(FileData(name = name, bytes = bytes, mimeType = mime))
                } catch (_: Throwable) {
                    if (cont.isActive) cont.resume(null)
                } finally {
                    launcher.unregister()
                }
            }
            launcher.launch(arrayOf("*/*"))
            cont.invokeOnCancellation {
                launcher.unregister()
            }
        }
    }
}
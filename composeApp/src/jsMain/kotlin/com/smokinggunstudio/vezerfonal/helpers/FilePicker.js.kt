package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.browser.document
import kotlinx.coroutines.suspendCancellableCoroutine
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import kotlin.coroutines.resume

actual class FilePicker {
    actual suspend fun pickFile(): FileData? = suspendCancellableCoroutine { cont ->
        val input = (document.createElement("input") as HTMLInputElement).apply {
            type = "file"
            style.display = "none"
        }
        document.body?.appendChild(input)
        var listener: ((Event) -> Unit)? = null
        fun cleanup() {
            listener?.let { input.removeEventListener("change", it) }
            input.parentElement?.removeChild(input)
        }
        listener = l@{ _ ->
            val file = input.files?.item(0)
            if (file == null) {
                cleanup()
                if (cont.isActive) cont.resume(null)
                return@l
            }
            val reader = FileReader()
            reader.onload = {
                try {
                    val result = reader.result as ArrayBuffer
                    val u8 = Uint8Array(result)
                    val len = u8.length
                    val bytes = ByteArray(len)
                    for (i in 0 until len) {
                        bytes[i] = (u8.asDynamic()[i] as Int).toByte()
                    }
                    val name = file.name
                    val mime = file.type.ifEmpty { "application/octet-stream" }
                    cleanup()
                    if (cont.isActive) cont.resume(FileData(bytes = bytes, FileMetaData(name = name, mimeType = mime)))
                } catch (_: Throwable) {
                    cleanup()
                    if (cont.isActive) cont.resume(null)
                }
            }
            reader.onerror = {
                cleanup()
                if (cont.isActive) cont.resume(null)
            }
            reader.readAsArrayBuffer(file)
        }
        input.addEventListener("change", listener)
        cont.invokeOnCancellation { cleanup() }
        input.click()
    }
}
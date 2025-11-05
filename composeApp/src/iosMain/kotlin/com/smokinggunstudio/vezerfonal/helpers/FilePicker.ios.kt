package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTTypeData
import platform.posix.memcpy
import kotlin.coroutines.resume

actual class FilePicker {
    actual suspend fun pickFile(): FileData? = suspendCancellableCoroutine { cont ->
        val picker = UIDocumentPickerViewController(forOpeningContentTypes = listOf(UTTypeData))
        val delegate = object : platform.darwin.NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
                val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
                if (url == null) {
                    if (cont.isActive) cont.resume(null)
                    return
                }
                url.startAccessingSecurityScopedResource()
                val data: NSData? = NSData.dataWithContentsOfURL(url)
                url.stopAccessingSecurityScopedResource()
                if (data != null) {
                    val bytes = data.toByteArray()
                    val name = url.lastPathComponent ?: "file"
                    val mime = "application/octet-stream"
                    if (cont.isActive) cont.resume(FileData(name = name, bytes = bytes, mimeType = mime))
                } else {
                    if (cont.isActive) cont.resume(null)
                }
            }
            override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                if (cont.isActive) cont.resume(null)
            }
        }
        picker.delegate = delegate
        val presenter = topViewController()
        if (presenter != null) {
            presenter.presentViewController(picker, animated = true, completion = null)
        } else {
            if (cont.isActive) cont.resume(null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val lengthInt = this.length.toInt()
    val result = ByteArray(lengthInt)
    result.usePinned { pinned ->
        memcpy(pinned.addressOf(0), this.bytes, this.length)
    }
    return result
}

private fun topViewController(): UIViewController? {
    val app = UIApplication.sharedApplication
    val windows = app.windows
    for (window in windows) {
        val uiWindow = window as? platform.UIKit.UIWindow ?: continue
        val root = uiWindow.rootViewController
        if (root != null) return findTop(root)
    }
    return null
}

private fun findTop(controller: UIViewController): UIViewController {
    var current = controller
    while (current.presentedViewController != null) {
        current = current.presentedViewController!!
    }
    return current
}
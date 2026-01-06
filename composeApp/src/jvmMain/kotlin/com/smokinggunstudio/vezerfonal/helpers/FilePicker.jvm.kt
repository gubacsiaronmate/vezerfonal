package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLConnection
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.SwingUtilities

actual class FilePicker {
    actual suspend fun pickFile(): FileData? = withContext(Dispatchers.IO) {
        val chooser = JFileChooser()
        val result = withContext(Dispatchers.Default) {
            // Show dialog on EDT synchronously
            val resHolder = IntArray(1)
            @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
            val lock = Object()
            var done = false
            SwingUtilities.invokeLater {
                resHolder[0] = chooser.showOpenDialog(null)
                synchronized(lock) {
                    done = true
                    lock.notifyAll()
                }
            }
            synchronized(lock) {
                while (!done) lock.wait()
            }
            resHolder[0]
        }
        if (result == JFileChooser.APPROVE_OPTION) {
            val file: File = chooser.selectedFile ?: return@withContext null
            val bytes = file.readBytes()
            val name = file.name
            val mimeType = probeMimeType(file.toPath()) ?: URLConnection.guessContentTypeFromName(name) ?: "application/octet-stream"
            FileData(bytes = bytes, FileMetaData(name = name, mimeType = mimeType))
        } else {
            null
        }
    }
}

private fun probeMimeType(path: Path): String? = try {
    Files.probeContentType(path)
} catch (_: Throwable) {
    null
}
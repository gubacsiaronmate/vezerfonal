@file:OptIn(ExperimentalWasmJsInterop::class)

package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.coroutines.await
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.Promise

// Returns a Promise resolving to "base64bytes|filename|mimeType", or "" on cancel/error.
// Empty string is used as the null sentinel — Promise<JsString> avoids the nullable type-param
// issue in Kotlin/Wasm where Promise<JsString?> confuses the flow analyser (unreachable warnings).
@JsFun("""() => new Promise((resolve) => {
    const input = document.createElement('input');
    input.type = 'file';
    input.style.display = 'none';
    document.body.appendChild(input);
    input.addEventListener('change', () => {
        const file = input.files && input.files[0];
        document.body.removeChild(input);
        if (!file) { resolve(''); return; }
        const reader = new FileReader();
        reader.onload = (e) => {
            try {
                const arr = new Uint8Array(e.target.result);
                const chunks = [];
                const chunkSize = 8192;
                for (let i = 0; i < arr.length; i += chunkSize) {
                    chunks.push(String.fromCharCode(...arr.subarray(i, i + chunkSize)));
                }
                resolve(btoa(chunks.join('')) + '|' + file.name + '|' + (file.type || 'application/octet-stream'));
            } catch (err) { resolve(''); }
        };
        reader.onerror = () => resolve('');
        reader.readAsArrayBuffer(file);
    });
    input.click();
})""")
private external fun jsPickFilePromise(): Promise<JsString>

@OptIn(ExperimentalEncodingApi::class)
actual class FilePicker {
    actual suspend fun pickFile(): FileData? {
        val str = jsPickFilePromise().await<JsString>().toString()
        if (str.isEmpty()) return null
        val pipeIdx1 = str.indexOf('|')
        val pipeIdx2 = if (pipeIdx1 >= 0) str.indexOf('|', pipeIdx1 + 1) else -1
        if (pipeIdx1 < 0 || pipeIdx2 < 0) return null
        return try {
            val bytes = Base64.decode(str.substring(0, pipeIdx1))
            val name = str.substring(pipeIdx1 + 1, pipeIdx2)
            val mime = str.substring(pipeIdx2 + 1)
            FileData(bytes, FileMetaData(name = name, mimeType = mime))
        } catch (_: Exception) { null }
    }
}

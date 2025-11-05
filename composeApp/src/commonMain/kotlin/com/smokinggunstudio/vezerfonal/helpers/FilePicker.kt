package com.smokinggunstudio.vezerfonal.helpers

expect class FilePicker() {
    suspend fun pickFile(): FileData?
}
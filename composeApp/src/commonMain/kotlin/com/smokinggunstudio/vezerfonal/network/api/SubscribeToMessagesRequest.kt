package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json

suspend fun subscribeToMessages(
    client: HttpClient,
    accessToken: String,
    onMessage: CallbackFunction<MessageData>,
    onError: CallbackFunction<Throwable> = CallbackFunction { throw it }
) {
    try {
        client
            .prepareGet(NetworkConstants.Endpoints.SUBSCRIBE_TO_MESSAGES) {
                bearerAuth(accessToken)
            }.execute { response ->
                val channel = response.bodyAsChannel()
                
                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line() ?: continue
                    
                    if(line.isNotBlank()) try {
                        val message = Json.decodeFromString<MessageData>(line)
                        onMessage(message)
                    } catch (e: Exception) { onError(e) }
                }
            }
    } catch (e: Exception) { onError(e) }
}
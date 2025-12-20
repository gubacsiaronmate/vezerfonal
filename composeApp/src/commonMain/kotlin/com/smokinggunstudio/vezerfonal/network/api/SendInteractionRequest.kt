package com.smokinggunstudio.vezerfonal.network.api

import androidx.compose.foundation.interaction.Interaction
import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.unaryPlus
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

suspend fun sendInteraction(
    accessToken: String,
    interaction: InteractionInfoData,
    client: HttpClient
): Boolean {
    val url = "${NetworkConstants.Endpoints.INTERACTIONS}/${interaction.type.name.lowercase()}/send"
    
    val response = client
        .post(url) {
            bearerAuth(accessToken)
            setBody(interaction)
        }
    
    return when (val status = response.status) {
        HttpStatusCode.OK -> true
        HttpStatusCode.Unauthorized ->
            throw UnauthorizedException()
        else -> throw UnableToLoadException(status)
    }
}
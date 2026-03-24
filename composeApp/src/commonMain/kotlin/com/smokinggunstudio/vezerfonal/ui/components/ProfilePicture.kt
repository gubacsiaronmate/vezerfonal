package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.FileMetaData
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.network.api.getProfilePicture
import com.smokinggunstudio.vezerfonal.network.helpers.NetworkConstants
import com.smokinggunstudio.vezerfonal.ui.helpers.svgXMLToByteArray
import com.smokinggunstudio.vezerfonal.ui.helpers.toImageResource
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import kotlin.math.roundToInt

@Composable
fun ProfilePicture(
    name: String = "",
    size: Dp = 48.dp,
    profilePicFilename: String? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier
) {
    val client = LocalHttpClient.current
    var data: FileData? by remember { mutableStateOf(null) }
    var loading by remember { mutableStateOf(false) }
    val pxSize = with(LocalDensity.current) { size.toPx() }.roundToInt()

    LaunchedEffect(profilePicFilename ?: name) {
        loading = true
        data = try {
            if (profilePicFilename != null) {
                val url = "${NetworkConstants.BASE_URL}${NetworkConstants.Endpoints.GET_PFP}$profilePicFilename"
                val response = client.get(url)
                val bytes = response.body<ByteArray>()
                val mime = response.headers[HttpHeaders.ContentType] ?: "image/jpeg"
                FileData(bytes, FileMetaData(profilePicFilename, mime))
            } else {
                getProfilePicture(name, pxSize)
            }
        } catch (e: Throwable) {
            log { e.message.orEmpty() }
            null
        }
        loading = false
    }

    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: ""
        val fontSize = (size.value * 0.4f).sp

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                )
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center,
        ) {
            if (loading) CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.6f))
            else data?.let {
                if (it.metaData.mimeType == "image/svg+xml")
                    Image(
                        bitmap = it.svgXMLToByteArray(pxSize),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                else Image(
                    bitmap = it.toImageResource(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: if (initial.isNotEmpty()) {
                Text(
                    text = initial,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            } else {
                Image(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.6f),
                )
            }
        }
    }
}

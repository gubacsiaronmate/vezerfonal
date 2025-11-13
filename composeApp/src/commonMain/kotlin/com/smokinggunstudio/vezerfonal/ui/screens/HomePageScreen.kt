package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.FilePicker
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.registerBasic
import com.smokinggunstudio.vezerfonal.ui.components.DismissibleSnackBar
import com.smokinggunstudio.vezerfonal.ui.components.ListItem
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.filter
import vezerfonal.composeapp.generated.resources.spiralgraphic
import vezerfonal.composeapp.generated.resources.vezerfonal
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun HomePageScreen(
    tokenStorage: TokenStorage,
    client: HttpClient
) {
    val scope = rememberCoroutineScope()
    var tokens: TokenResponse? by remember { mutableStateOf(null) }
    scope.launch {
        tokens = tokenStorage.getTokens()
        
        if (tokens == null) {
            println("Tokens is null: ${tokens == null}")
            println("Trying to request tokens to test...")
            val filePicker = FilePicker()
            
            val testTokens = registerBasic(
                userData = UserData(
                    registrationCode = "996633",
                    email = "asd${(1..100_000).random()}@gmail.com",
                    password = Uuid.random().toString(),
                    name = "My name is John Cena",
                    identifier = Uuid.random().toString(),
                    isSuperAdmin = false
                ),
                rememberMe = true,
                pfp = filePicker.pickFile() ?: error("File is not picked"),
                client = client
            )
            
            tokenStorage.saveTokens(testTokens)
            
            println("Trying to get stored tokens again...")
            tokens = tokenStorage.getTokens()
            println("Tokens is null: ${tokens == null}")
            
        } else println("Tokens is null: ${tokens == null}")
    }
    var isVisible by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                stringResource(Res.string.vezerfonal),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        
        Image(
            painter = painterResource(Res.drawable.spiralgraphic),
            contentDescription = "Home Page Image",
            modifier = Modifier.fillMaxWidth()
                .height(210.dp),
            contentScale = ContentScale.FillWidth
        )
        
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.clickable(onClick = { isVisible = true })
                    .padding(8.dp)
            ) {
                Text(stringResource(Res.string.filter),
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    imageVector = Icons.Filled.Filter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant))
            }
        }
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
        )
        
        DismissibleSnackBar(isVisible) {
            Text("Token is null: ${tokens == null}")
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            for (i in 0..10) {
                ListItem(
                    title = "Pelda$i",
                    author = "PeldaAuthor$i",
                    onClick = {}
                )
            }
        }
    }
}
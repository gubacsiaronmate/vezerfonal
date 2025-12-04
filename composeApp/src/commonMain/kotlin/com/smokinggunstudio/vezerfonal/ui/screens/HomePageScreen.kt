package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.ui.components.FilterApplyCancelButtons
import com.smokinggunstudio.vezerfonal.ui.components.FilterButton
import com.smokinggunstudio.vezerfonal.ui.components.ListItem
import com.smokinggunstudio.vezerfonal.ui.components.MessageFilter
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.filter
import vezerfonal.composeapp.generated.resources.spiralgraphic
import vezerfonal.composeapp.generated.resources.vezerfonal
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun HomePageScreen(
    accessToken: String,
    client: HttpClient,
    scrollLockedBySliderCallback: CallbackEvent<Boolean>
) {
    val scope = rememberCoroutineScope()
    var isFilterOpened by remember { mutableStateOf(false) }
    val messageFilterState = remember { MessageFilterState() }
    
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
            if (!isFilterOpened) FilterButton { isFilterOpened = true }
            else FilterApplyCancelButtons(
                onApply = { _ -> isFilterOpened = false },
                onCancel = { isFilterOpened = false }
            )
        }
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
        )
        
        Box(modifier = Modifier.fillMaxSize()) {
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
            
            if (isFilterOpened)
                MessageFilter(
                    state = messageFilterState,
                    tabOpenedCallback = {  },
                    modifier = Modifier.align(Alignment.TopCenter)
                ) { scrollLockedBySliderCallback(it && isFilterOpened) }
            else scrollLockedBySliderCallback(false)
        }
    }
}
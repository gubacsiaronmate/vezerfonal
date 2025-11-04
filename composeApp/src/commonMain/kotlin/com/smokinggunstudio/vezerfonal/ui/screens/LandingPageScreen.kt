package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.register
import vezerfonal.composeapp.generated.resources.scene_1
import vezerfonal.composeapp.generated.resources.vezerfonal

@Composable
fun LandingPageScreen(
    onRegisterClick: ClickEvent,
    onLoginClick: ClickEvent
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    
    ) {
        Image(
            painter = painterResource(Res.drawable.scene_1),
            contentDescription = "Landing Page Image",
            modifier = Modifier.size(100.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, bottom = 15.dp)
        ) {
            Text(
                text = stringResource(Res.string.vezerfonal),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )
        }
        Button(onClick = onRegisterClick) {
            Text(text = stringResource(Res.string.register))
            
        }
    }
}
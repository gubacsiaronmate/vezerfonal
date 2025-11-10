package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.filter
import vezerfonal.composeapp.generated.resources.spiralgraphic
import vezerfonal.composeapp.generated.resources.vezerfonal

@Composable
fun HomePageScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(stringResource(Res.string.vezerfonal))
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth()
            .height(1.dp))
        Image(
            painterResource(Res.drawable.spiralgraphic),
            contentDescription = "Home Page Image"
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth()
            .height(1.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End) {
            Button(
                 onClick = {}
            ) {
                Text(stringResource(Res.string.filter))
            }
            Image(imageVector = Icons.Filled.Filter, contentDescription = null)
        }
    }
    HorizontalDivider(modifier = Modifier.fillMaxWidth()
    .height(1.dp))
}
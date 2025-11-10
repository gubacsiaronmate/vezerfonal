package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ListItem(
    title: String,
    author: String,
    onClick: ClickEvent
) {
    Row(modifier = Modifier
        .clickable { onClick() }
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Image(imageVector = (Icons.Filled.Person),
                contentDescription = null)
        }
    }
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Text(text = title)
        Text(text = author)
    }
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Image(imageVector = (Icons.Filled.ArrowRight), contentDescription = null)
    }
}
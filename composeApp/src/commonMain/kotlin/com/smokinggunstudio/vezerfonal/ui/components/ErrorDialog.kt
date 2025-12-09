package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.ui.navigation.Landing
import com.smokinggunstudio.vezerfonal.ui.screens.LandingPageScreen
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.error_happened
import vezerfonal.composeapp.generated.resources.leave
import vezerfonal.composeapp.generated.resources.login

@Composable fun ErrorDialog(
    errorMessage: String,
    isUnauthed: Boolean,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow

    Dialog(modifier = modifier) {
        Text(
            text = stringResource(Res.string.error_happened),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        Text(errorMessage)
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            onClick = {
                if (navigator.lastItem != Landing)
                    if (isUnauthed)
                        navigator.replaceAll(Landing)
                    else
                        navigator.popUntilRoot()
                else throw Exception("No way to reliably exit apps so just fail.")
            },
        ) {
            if (isUnauthed)
                Text(stringResource(Res.string.login))
            else
                Text(stringResource(Res.string.leave))
        }
    }
}
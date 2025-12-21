package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.ui.helpers.HomeCache
import com.smokinggunstudio.vezerfonal.ui.helpers.exitApp
import com.smokinggunstudio.vezerfonal.ui.navigation.Landing
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
                HomeCache.invalidate()

                if (navigator.lastItem !is Landing)
                    if (isUnauthed)
                        navigator.replaceAll(Landing)
                    else navigator.popUntilRoot()
                else exitApp() /*throw Exception("No way to reliably exit apps so just fail.")*/
            },
        ) {
            if (isUnauthed && navigator.lastItem !is Landing)
                Text(stringResource(Res.string.login))
            else Text(stringResource(Res.string.leave))
        }
    }
}
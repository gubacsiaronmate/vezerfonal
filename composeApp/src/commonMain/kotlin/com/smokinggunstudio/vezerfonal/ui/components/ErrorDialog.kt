package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.helpers.UnableToLoadException
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.UserNotFoundException
import com.smokinggunstudio.vezerfonal.ui.helpers.HomeCache
import com.smokinggunstudio.vezerfonal.ui.helpers.exitApp
import com.smokinggunstudio.vezerfonal.ui.navigation.Landing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.error_happened
import vezerfonal.composeapp.generated.resources.leave
import vezerfonal.composeapp.generated.resources.login

@Composable fun ErrorDialog(
    error: Throwable,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    val isUnauthed = error is UnauthorizedException

    Dialog(modifier = modifier) {
        Text(
            text = stringResource(Res.string.error_happened),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        
        when (error) {
            is UnauthorizedException,
            is UnableToLoadException,
            is UserNotFoundException
                -> error.message?.let { Text(it) }
        }
        
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            onClick = {
                HomeCache.invalidate()

                if (navigator.lastItem is Landing) exitApp()
                else navigator.popUntilRoot()
            },
        ) {
            val strRes = if (isUnauthed)
                Res.string.login
            else Res.string.leave
            
            Text(stringResource(strRes))
        }
    }
}
package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.ShapeModifier
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun LandingPageScreen(
    onRegisterClick: Function,
    onLoginClick: Function,
) {
    val windowSizeInfo = LocalWindowSizeInfo.current
    val isWide = windowSizeInfo.widthClass != WindowWidthClass.Compact
    val isDark = isSystemInDarkTheme()

    val heroImage = @Composable { modifier: Modifier ->
        Image(
            painter = painterResource(
                if (!isDark) Res.drawable.scene_1 else Res.drawable.scene_2
            ),
            contentDescription = null,
            modifier = modifier,
        )
    }

    val textAndButtons = @Composable {
        Text(
            text = stringResource(Res.string.vezerfonal),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(Spacing.xl))
        AnimatedButton(
            onClick = onRegisterClick,
            shape = ShapeModifier.ROUNDED.toShape(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.register),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(Spacing.md))
        AnimatedButton(
            onClick = onLoginClick,
            shape = ShapeModifier.ROUNDED.toShape(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.login),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
    }

    if (isWide) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(Spacing.xxl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            heroImage(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Spacing.xxl),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                textAndButtons()
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = Spacing.lg),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            heroImage(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                textAndButtons()
            }
        }
    }
}

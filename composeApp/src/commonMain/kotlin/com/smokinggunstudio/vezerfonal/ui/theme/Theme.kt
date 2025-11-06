package com.smokinggunstudio.vezerfonal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val darkColorScheme = darkColorScheme(
    primary = Keppel,
    onPrimary = White,
    secondary = DodgerBlue,
    onSecondary = White,
    tertiary = MediumAquamarine,
    onTertiary = RichBlack,
    background = EerieBlack,
    onBackground = AliceBlueGray,
    surface = RaisinBlack,
    onSurface = Cultured,
    surfaceVariant = CharcoalGray,
)

val lightColorScheme = lightColorScheme(
    primary = CaribbeanGreen,
    onPrimary = White,
    secondary = AzureRadiance,
    onSecondary = White,
    tertiary = DarkGreen,
    onTertiary = White,
    background = GhostWhite,
    onBackground = OxfordBlue,
    surface = White,
    onSurface = Gunmetal,
    surfaceVariant = LightGray,
)

@Composable fun VezerfonalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
//    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
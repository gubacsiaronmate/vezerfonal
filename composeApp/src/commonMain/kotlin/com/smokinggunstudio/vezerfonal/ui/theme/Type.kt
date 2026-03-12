package com.smokinggunstudio.vezerfonal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import vezerfonal.composeapp.generated.resources.DMSans_italic_variable
import vezerfonal.composeapp.generated.resources.DMSans_variable
import vezerfonal.composeapp.generated.resources.Res

@Composable
fun rememberAppTypography(): Typography {
    val dmSans = FontFamily(
        Font(Res.font.DMSans_variable, weight = FontWeight.Thin),
        Font(Res.font.DMSans_variable, weight = FontWeight.ExtraLight),
        Font(Res.font.DMSans_variable, weight = FontWeight.Light),
        Font(Res.font.DMSans_variable, weight = FontWeight.Normal),
        Font(Res.font.DMSans_variable, weight = FontWeight.Medium),
        Font(Res.font.DMSans_variable, weight = FontWeight.SemiBold),
        Font(Res.font.DMSans_variable, weight = FontWeight.Bold),
        Font(Res.font.DMSans_variable, weight = FontWeight.ExtraBold),
        Font(Res.font.DMSans_italic_variable, weight = FontWeight.Normal,  style = FontStyle.Italic),
        Font(Res.font.DMSans_italic_variable, weight = FontWeight.Medium,  style = FontStyle.Italic),
        Font(Res.font.DMSans_italic_variable, weight = FontWeight.SemiBold, style = FontStyle.Italic),
        Font(Res.font.DMSans_italic_variable, weight = FontWeight.Bold,    style = FontStyle.Italic),
    )

    return Typography(
        displayLarge  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal,   fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
        displayMedium = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal,   fontSize = 45.sp, lineHeight = 52.sp),
        displaySmall  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal,   fontSize = 36.sp, lineHeight = 44.sp),
        headlineLarge  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 40.sp),
        headlineMedium = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp),
        headlineSmall  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp),
        titleLarge  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
        titleMedium = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
        titleSmall  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
        bodyLarge   = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
        bodyMedium  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
        bodySmall   = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
        labelLarge  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
        labelMedium = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium,   fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
        labelSmall  = TextStyle(fontFamily = dmSans, fontWeight = FontWeight.Medium,   fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    )
}

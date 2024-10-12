package com.example.vociapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ColorPalette.PrimaryDark,
    onPrimary = ColorPalette.OnPrimaryDark,
    primaryContainer = ColorPalette.PrimaryContainerDark,
    onPrimaryContainer = ColorPalette.OnPrimaryContainerDark,
    secondary = ColorPalette.SecondaryDark,
    onSecondary = ColorPalette.OnSecondaryDark,
    secondaryContainer = ColorPalette.SecondaryContainerDark,
    onSecondaryContainer = ColorPalette.OnSecondaryContainerDark,
    tertiary = ColorPalette.TertiaryDark,
    onTertiary = ColorPalette.OnTertiaryDark,
    tertiaryContainer = ColorPalette.TertiaryContainerDark,
    onTertiaryContainer = ColorPalette.OnTertiaryContainerDark,
    error = ColorPalette.ErrorDark,
    onError = ColorPalette.OnErrorDark,
    errorContainer = ColorPalette.ErrorContainerDark,
    onErrorContainer = ColorPalette.OnErrorContainerDark,
    background = ColorPalette.BackgroundDark,
    onBackground = ColorPalette.OnBackgroundDark,
    surface = ColorPalette.SurfaceDark,
    onSurface = ColorPalette.OnSurfaceDark,
    surfaceVariant = ColorPalette.SurfaceVariantDark,
    onSurfaceVariant = ColorPalette.OnSurfaceVariantDark,
    outline = ColorPalette.OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = ColorPalette.PrimaryLight,
    onPrimary = ColorPalette.OnPrimaryLight,
    primaryContainer = ColorPalette.PrimaryContainerLight,
    onPrimaryContainer = ColorPalette.OnPrimaryContainerLight,
    secondary = ColorPalette.SecondaryLight,
    onSecondary = ColorPalette.OnSecondaryLight,
    secondaryContainer = ColorPalette.SecondaryContainerLight,
    onSecondaryContainer = ColorPalette.OnSecondaryContainerLight,
    tertiary = ColorPalette.TertiaryLight,
    onTertiary = ColorPalette.OnTertiaryLight,
    tertiaryContainer = ColorPalette.TertiaryContainerLight,
    onTertiaryContainer = ColorPalette.OnTertiaryContainerLight,
    error = ColorPalette.ErrorLight,
    onError = ColorPalette.OnErrorLight,
    errorContainer = ColorPalette.ErrorContainerLight,
    onErrorContainer = ColorPalette.OnErrorContainerLight,
    background = ColorPalette.BackgroundLight,
    onBackground = ColorPalette.OnBackgroundLight,
    surface = ColorPalette.SurfaceLight,
    onSurface = ColorPalette.OnSurfaceLight,
    surfaceVariant = ColorPalette.SurfaceVariantLight,
    onSurfaceVariant = ColorPalette.OnSurfaceVariantLight,
    outline = ColorPalette.OutlineLight
)

@Composable
fun VociAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
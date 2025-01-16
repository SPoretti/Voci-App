package com.voci.app.ui.theme

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
    secondary = ColorPalette.SecondaryDark,
    onSecondary = ColorPalette.OnSecondaryDark,
    secondaryContainer = ColorPalette.SecondaryContainerDark,
    error = ColorPalette.ErrorDark,
    onError = ColorPalette.OnErrorDark,
    background = ColorPalette.BackgroundDark,
    onBackground = ColorPalette.OnBackgroundDark,
    surface = ColorPalette.SurfaceDark,
    onSurface = ColorPalette.OnSurfaceDark,
    outline = ColorPalette.OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = ColorPalette.PrimaryLight,
    onPrimary = ColorPalette.OnPrimaryLight,
    primaryContainer = ColorPalette.PrimaryContainerLight,
    secondary = ColorPalette.SecondaryLight,
    onSecondary = ColorPalette.OnSecondaryLight,
    secondaryContainer = ColorPalette.SecondaryContainerLight,
    error = ColorPalette.ErrorLight,
    onError = ColorPalette.OnErrorLight,
    background = ColorPalette.BackgroundLight,
    onBackground = ColorPalette.OnBackgroundLight,
    surface = ColorPalette.SurfaceLight,
    onSurface = ColorPalette.OnSurfaceLight,
    outline = ColorPalette.OutlineLight
)

@Composable
fun VociAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
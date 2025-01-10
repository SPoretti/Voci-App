# Theme

The MaterialTheme configuration is responsible for defining the application's visual identity. It adapts the app's colors and typography based on the user's theme preference (light or dark mode) and optionally supports dynamic colors on Android 12+ devices.

## Color Schemes

The theme uses two predefined color schemes to differentiate between light and dark themes. These schemes are based on the `ColorPalette` defined earlier.

### DarkColorScheme
```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = ColorPalette.PrimaryDark,
    onPrimary = ColorPalette.OnPrimaryDark,
    secondary = ColorPalette.SecondaryDark,
    onSecondary = ColorPalette.OnSecondaryDark,
    error = ColorPalette.ErrorDark,
    background = ColorPalette.BackgroundDark,
    onBackground = ColorPalette.OnBackgroundDark,
    surface = ColorPalette.SurfaceDark,
    onSurface = ColorPalette.OnSurfaceDark,
    outline = ColorPalette.OutlineDark
)
```

### LightColorScheme
```kotlin
private val LightColorScheme = lightColorScheme(
    primary = ColorPalette.PrimaryLight,
    onPrimary = ColorPalette.OnPrimaryLight,
    secondary = ColorPalette.SecondaryLight,
    onSecondary = ColorPalette.OnSecondaryLight,
    error = ColorPalette.ErrorLight,
    background = ColorPalette.BackgroundLight,
    onBackground = ColorPalette.OnBackgroundLight,
    surface = ColorPalette.SurfaceLight,
    onSurface = ColorPalette.OnSurfaceLight,
    outline = ColorPalette.OutlineLight
)
```

## VociAppTheme Composable

The `VociAppTheme` composable applies the appropriate color scheme and typography based on the user's preferences and device capabilities.

### Parameters
- **darkTheme**: Boolean value that determines whether to use the dark theme. Defaults to the system setting.
- **dynamicColor**: Boolean value that enables dynamic color schemes on devices running Android 12 or later. Defaults to `false`.
- **content**: Lambda function that provides the UI content to which the theme is applied.

### Implementation
```kotlin
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
```

## Notes
- The `dynamicColor` parameter uses the device's dynamic color capabilities for a more personalized appearance if supported.
- The `Typography` object defines the text styles used across the app, ensuring consistency in fonts and text sizes.
- The `MaterialTheme` composable wraps the app's content to apply the selected theme dynamically.

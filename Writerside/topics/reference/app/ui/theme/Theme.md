# Theme (Theme.kt)

## Overview

This file defines the theme for the application, including color schemes for both dark and light modes. It utilizes Jetpack Compose's `MaterialTheme` to apply these themes globally throughout the app. It also supports dynamic theming based on the system's dark mode settings and the Android version.

## Code Explanation

### Breakdown
- **`private val DarkColorScheme = darkColorScheme(...)`**
   - Defines the color scheme for dark mode.
   - Uses `darkColorScheme` to specify colors for various UI elements like primary, secondary, background, and error.

- **`private val LightColorScheme = lightColorScheme(...)`**
   - Defines the color scheme for light mode.
   - Uses `lightColorScheme` to specify colors for various UI elements similar to the dark mode setup.

- **`@Composable fun VociAppTheme(...) { ... }`**
   - This composable function sets up the theme using `MaterialTheme`.
   - Parameters:
      - `darkTheme`: Boolean flag to indicate whether dark theme is enabled. Defaults to system setting with `isSystemInDarkTheme()`.
      - `dynamicColor`: Boolean flag to indicate whether dynamic theming is enabled. Defaults to `false`.
      - `content`: Lambda that contains the UI content to which the theme will be applied.

- **`val colorScheme = when { ... }`**
   - Determines the appropriate color scheme based on the `darkTheme` and `dynamicColor` flags.
      - Uses `dynamicDarkColorScheme` or `dynamicLightColorScheme` for dynamic theming if supported (Android 12+).
      - Falls back to `DarkColorScheme` or `LightColorScheme` based on the `darkTheme` flag.

- **`MaterialTheme(...)`**
   - Applies the selected color scheme to the app's UI.
   - Parameters:
      - `colorScheme`: The chosen color scheme (either dark, light, or dynamic).
      - `typography`: Typography settings applied to the text throughout the app.
      - `content`: Lambda containing the composable content to which the theme is applied.

## Related Files

- **[ColorPalette.kt](Colors.md):** Defines the color palette used in the theme.
- **[Type.kt](Type.md):** Defines the typography settings used in the theme.
- **[MainActivity.kt](MainActivity.md):** Entry point of the app that applies the `VociAppTheme`.

## Usage

The `VociAppTheme` function is used to wrap the entire content of the app to apply the theme consistently. Here's how it is typically used:

### Applying the Theme
Wrap your composable content with the `VociAppTheme` to apply the theme:

```kotlin
setContent {
    VociAppTheme {
        // Composable content that uses the theme...
    }
}
```

### Dynamic and Dark Mode Theming
The theme can automatically adjust based on the system's dark mode setting and support dynamic colors on Android 12+:

```kotlin
@Composable
fun MyApp() {
    VociAppTheme(
        darkTheme = isSystemInDarkTheme(), // Automatically adjust to system's dark mode
        dynamicColor = true // Enable dynamic colors on supported devices
    ) {
        // UI content with applied dynamic theme
    }
}
```

## Additional Notes

- The dark and light color schemes are meticulously defined to ensure a cohesive and accessible UI.
- Dynamic theming allows the app to better integrate with the system themes available on newer Android devices.
- Usage of `MaterialTheme` ensures that the theme settings are propagated throughout the app, maintaining consistency.
# Typography (Type.kt)

## Overview

This file defines the typography styles for the application. It uses Jetpack Compose's `Typography` to set up the text styles, including font families, weights, sizes, line heights, and letter spacing. These styles can be applied globally to the app's text components.

## Code Explanation

### Breakdown
- **`val Typography = Typography(...)`**
    - Instantiates a `Typography` object that holds various text styles used throughout the app.
    - Customizes the default set of Material typography styles.

- **`bodyLarge = TextStyle(...)`**
    - Defines the text style for the body text of the application.
    - Parameters:
        - `fontFamily`: Sets the font family to `FontFamily.Default`.
        - `fontWeight`: Sets the font weight to `FontWeight.Normal`.
        - `fontSize`: Sets the font size to `16.sp`.
        - `lineHeight`: Sets the line height to `24.sp`.
        - `letterSpacing`: Sets the letter spacing to `0.5.sp`.

- **Commented Styles (e.g., `titleLarge`, `labelSmall`)**
    - Shows examples of other default text styles that can be overridden.
    - Each style can be customized similarly to `bodyLarge`.
    - Useful for defining consistent typography settings across different text components in the app.

## Related Files

- **[Theme.kt](Theme.md):** Applies the typography settings alongside the color schemes.
- **[ColorPalette.kt](Colors.md):** Defines the color palette used in the theme.
- **[MainActivity.kt](MainActivity.md):** Entry point of the app that applies the `VociAppTheme`.

## Usage

The `Typography` instance defined in `Type.kt` is used to maintain consistent text styles across the app. Here's how it is typically integrated:

### Applying Typography in Theme
Integrate the typography settings in your theme setup within `Theme.kt`:

```kotlin
MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography, // Apply the typography settings
    content = content
)
```

### Customizing Text Styles
You can define additional or override existing text styles by uncommenting and customizing the provided examples:

```kotlin
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Uncomment and customize as needed
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Add more text styles here
)
```

## Additional Notes

- Consistent typography enhances the readability and aesthetics of the app.
- Defining typography styles in a central file enables easy updates and ensures uniformity across different screens.
- Customize various text components, such as titles, body text, and labels, to fit the app's design requirements.
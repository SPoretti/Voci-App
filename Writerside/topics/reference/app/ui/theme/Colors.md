# Color Palette

This file defines the color palette used throughout the app. It provides color values for both light and dark themes, ensuring consistency and accessibility.

## Color Definitions

| Color Name             | Light Theme Value | Dark Theme Value | Description                                                      |
|------------------------|-------------------|------------------|------------------------------------------------------------------|
| Primary                | `0xFF035DFB`      | `0xFF035DFB`     | Main blue color, used for most of the app.                       |
| On Primary             | `0xFF000000`      | `0xFF000000`     | White color to contrast when put on top of primary.              |
| Primary Container      | `0x60035DFB`      | `0x60035DFB`     | Light version of the primary color to use as container.          |
| Secondary              | `0xFFFF5700`      | `0xFFFF5700`     | Orange color used inside the app                                 |
| On Secondary           | `0xFFFFFFFF`      | `0xFF000000`     | Black color to go on top of secondary when used as a background. |
| Secondary Container    | `0xFFCC7A00`      | `0xFFCC7A00`     | Dark version of the orange to use as container.                  |
| Error                  | `0xFFD32F2F`      | `0xFFEF5350`     | Color used for error states.                                     |
| On Error               | `0xFFFFFFFF`      | `0xFF000000`     | Color used for content on top of error backgrounds.              |
| Background             | `0xFFFAFAFA`      | `0xFF212121`     | The background color of the app.                                 |
| On Background          | `0xFF212121`      | `0xFFFFFFFF`     | Color used for content on top of the background.                 |
| Surface                | `0xFFFFFFFF`      | `0xFF424242`     | The surface color of UI elements.                                |
| On Surface             | `0xFF212121`      | `0xFFFFFFFF`     | Color used for content on top of surfaces.                       |
| Outline                | `0xFFBDBDBD`      | `0xFF757575`     | Color used for outlines and borders.                             |

## Usage

These color values can be accessed through the `ColorPalette` object in the `Colors.kt` file:

```kotlin
val primaryColor = ColorPalette.PrimaryLight // For light theme
val secondaryColor = ColorPalette.SecondaryDark // For dark theme
```

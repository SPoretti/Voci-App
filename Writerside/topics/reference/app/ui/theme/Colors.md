# Color Palette

This file defines the color palette used throughout the app. It provides color values for both light and dark themes, ensuring consistency and accessibility.

## Color Definitions

| Color Name             | Light Theme Value | Dark Theme Value | Description                                            |
|------------------------|-------------------|------------------|--------------------------------------------------------|
| Primary                | `0xFFFF9800`      | `0xFFFFA726`     | The main color of the app, used for primary elements.  |
| On Primary             | `0xFF000000`      | `0xFF000000`     | Color used for content on top of the primary color.    |
| Primary Container      | `0xFFFFE0B2`      | `0xFFE65100`     | Color used for containers with primary content.        |
| On Primary Container   | `0xFF613D00`      | `0xFFFFE0B2`     | Color used for content on top of primary containers.   |
| Secondary              | `0xFF2196F3`      | `0xFF64B5F6`     | A secondary color used for accents and highlights.     |
| On Secondary           | `0xFFFFFFFF`      | `0xFF000000`     | Color used for content on top of the secondary color.  |
| Secondary Container    | `0xFFBBDEFB`      | `0xFF1976D2`     | Color used for containers with secondary content.      |
| On Secondary Container | `0xFF0D3C61`      | `0xFFBBDEFB`     | Color used for content on top of secondary containers. |
| Tertiary               | `0xFF4CAF50`      | `0xFF81C784`     | A tertiary color used for additional accents.          |
| On Tertiary            | `0xFFFFFFFF`      | `0xFF000000`     | Color used for content on top of the tertiary color.   |
| Tertiary Container     | `0xFFC8E6C9`      | `0xFF2E7D32`     | Color used for containers with tertiary content.       |
| On Tertiary Container  | `0xFF1B5E20`      | `0xFFC8E6C9`     | Color used for content on top of tertiary containers.  |
| Error                  | `0xFFD32F2F`      | `0xFFEF5350`     | Color used for error states.                           |
| On Error               | `0xFFFFFFFF`      | `0xFF000000`     | Color used for content on top of error backgrounds.    |
| Error Container        | `0xFFFFCDD2`      | `0xFFB71C1C`     | Color used for containers with error content.          |
| On Error Container     | `0xFF641414`      | `0xFFFFCDD2`     | Color used for content on top of error containers.     |
| Background             | `0xFFFAFAFA`      | `0xFF212121`     | The background color of the app.                       |
| On Background          | `0xFF212121`      | `0xFFFFFFFF`     | Color used for content on top of the background.       |
| Surface                | `0xFFFFFFFF`      | `0xFF424242`     | The surface color of UI elements.                      |
| On Surface             | `0xFF212121`      | `0xFFFFFFFF`     | Color used for content on top of surfaces.             |
| Surface Variant        | `0xFFEEEEEE`      | `0xFF616161`     | A variant of the surface color.                        |
| On Surface Variant     | `0xFF757575`      | `0xFFEEEEEE`     | Color used for content on top of surface variants.     |
| Outline                | `0xFFBDBDBD`      | `0xFF757575`     | Color used for outlines and borders.                   |

## Usage

These color values can be accessed through the `ColorPalette` object in your code:

```kotlin
val primaryColor = ColorPalette.PrimaryLight // For light theme
val secondaryColor = ColorPalette.SecondaryDark // For dark theme
```

# Custom Chip [Component]

The `CustomChip` component is a modular, clickable chip UI element used to display a small text label with an optional icon. It allows customization of background color and offers a callback function when clicked, making it a flexible component for various UI scenarios.

---

## Overview

- **Purpose**: Displays a customizable text label with an optional icon, designed to be used as a clickable chip in different screens.
- **Key Features**:
    - Displays a customizable text label with optional icon.
    - Handles chip click events via the `onClick` callback.
    - Supports customizable background color and rounded corners.

---

## Parameters

| Parameter         | Type           | Description                                           |
|-------------------|----------------|-------------------------------------------------------|
| `text`            | `String`       | Text to display inside the chip.                      |
| `onClick`         | `() -> Unit`   | Callback function triggered when the chip is clicked. |
| `imageVector`     | `ImageVector?` | Optional icon to display inside the chip.             |
| `backgroundColor` | `Color`        | Background color of the chip (default is surface).    |

---

## Usage Example

```kotlin
CustomChip(
    text = "Chip Label",
    onClick = { 
        // Handle chip click action
    },
    imageVector = Icons.Default.Person, // Optional icon
    // Optional Background color
    backgroundColor = MatherialTheme.colorpalette.surfaceVariant
)
```

---

## Features

1. **Customizable Text**:
    - Displays a customizable text label passed as the `text` parameter.
    - Text is styled using `MaterialTheme.typography.labelSmall` with overflow handling and a maximum of one line.

2. **Optional Icon**:
    - Supports an optional icon displayed on the left side of the text if the `imageVector` parameter is provided.
    - The icon is tinted with the primary color from the current `MaterialTheme`.

3. **Click Handling**:
    - The chip is clickable, with the `onClick` callback triggered when the chip is tapped.

4. **Custom Background Color**:
    - The chip's background color is customizable via the `backgroundColor` parameter, defaulting to `MaterialTheme.colorScheme.surface`.

5. **Rounded Corners**:
    - The chip has rounded corners with a radius of `16.dp`, defined by the `RoundedCornerShape`.

6. **Modular Design**:
    - Designed to be used as a reusable component in different screens within the app.

---

## Known Limitations

- **Icon Display**: If no `imageVector` is provided, the chip will only display the text without an icon.
- **Text Overflow**: If the text is too long, it will be truncated with ellipsis (`...`) and may not be fully visible.

---

## Notes

- **Dependencies**:
    - No external dependencies; relies on Jetpack Compose’s `MaterialTheme` and core Compose UI elements like `Surface`, `Row`, `Text`, and `Icon`.

- **Future Improvements**:
    - Add support for dynamic styling options like changing font size, weight, or adding more icons.
    - Include an optional trailing icon or action.




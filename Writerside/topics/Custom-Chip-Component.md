# Custom Chip [Component]

The `CustomChip` component is a customizable UI element that displays a chip with text and optionally an icon. It is designed to be reusable across different screens in the application, providing a consistent user interface element.

---

## Overview

- **Purpose**: Displays a chip with text and an optional icon, which can be clicked.
- **Key Features**:
    - Displays text with an optional icon.
    - Includes a click handler to perform actions when the chip is clicked.
    - Supports a rounded shape with customized colors.

---

## Parameters

| Parameter     | Type           | Description                                        |
|---------------|----------------|----------------------------------------------------|
| `text`        | `String`       | The text to be displayed on the chip.              |
| `onClick`     | `() -> Unit`   | Callback to handle the click event on the chip.    |
| `imageVector` | `ImageVector?` | Optional parameter to display an icon on the chip. |

---

## Usage Example

```kotlin
CustomChip(
    text = "Chip Label",
    onClick = { 
        // Handle chip click action
    },
    imageVector = Icons.Default.Person // Optional icon
)
```

---

## Features

1. **Chip Layout**:
    - Displays a chip with rounded corners.
    - The chip contains either a text label, or both a text label and an icon.

2. **Icon Support**:
    - If provided, an icon will be displayed next to the text on the chip.
    - The icon is colorized using the primary color from the theme.

3. **Click Handling**:
    - The chip is clickable and triggers the `onClick` callback when clicked.

4. **Styling**:
    - The chip is styled with a rounded shape (`16.dp` corner radius) and a surface variant background color.
    - Padding is applied to both the text and the optional icon.

---

**Note**: This component is highly reusable and can be styled further using the `modifier` parameter to adjust its appearance and layout.

---


# Custom FAB [Component]

The `CustomFAB` component is a customizable Floating Action Button (FAB) that combines both an icon and text. It provides an elevated button with a primary color scheme that can be tapped to trigger a specific action.

---

## Overview

- **Purpose**: Provides a Floating Action Button with both text and an icon for a more informative user action.
- **Key Features**:
    - Displays both an icon and text on the button.
    - Supports custom styling and positioning using the `modifier` parameter.
    - Includes haptic feedback when clicked.
    - Elevation effect to make the button stand out.

---

## Parameters

| Parameter      | Type                     | Description                                      |
|----------------|--------------------------|--------------------------------------------------|
| `text`         | `String`                 | Text to display on the button.                   |
| `icon`         | `ImageVector`            | Icon to display on the button.                   |
| `onClick`      | `() -> Unit`             | Action to perform when the button is clicked.    |
| `modifier`     | `Modifier`               | Optional modifier for styling and positioning.   |

---

## Usage Example

```kotlin
CustomFAB(
    text = "Add Item",
    icon = Icons.Default.Add,
    onClick = { /* Handle action */ },
    modifier = Modifier.align(Alignment.BottomEnd)
)
```

---

## Features

1. **Text and Icon Display**:
    - Displays both text and an icon inside the Floating Action Button.
    - The icon and text are aligned horizontally with padding for proper spacing.

2. **Customization**:
    - The `modifier` parameter allows for custom positioning, sizing, and additional styling of the FAB.
    - The button’s elevation can be adjusted using `FloatingActionButtonDefaults.elevation`.

3. **Haptic Feedback**:
    - Haptic feedback is triggered when the button is clicked, providing a tactile response to the user.

4. **Styling**:
    - The FAB uses the primary color from the `MaterialTheme.colorScheme` for its background color and the text/icon color for consistency with the app's design.
    - The `Text` inside the button uses the `titleMedium` style from the `MaterialTheme.typography` to ensure proper typography consistency.

5. **Accessibility**:
    - The icon’s `contentDescription` is set to the button’s `text` for improved accessibility and screen reader support.

---

**Note**: The `CustomFAB` is highly customizable through the `modifier` parameter, which can be used to adjust its size, position, and behavior, providing a flexible UI element that fits various use cases in the app.

---


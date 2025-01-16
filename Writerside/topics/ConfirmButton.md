# ConfirmButton [Component]

The `ConfirmButton` component is a customizable button designed for confirmation actions. It provides a visually distinct button with a primary color scheme and a clear label.

---

## Overview

- **Purpose**: To present a visually prominent button for confirmation actions, such as saving, submitting, or approving.
- **Key Features**:
    - Uses the primary color scheme from `MaterialTheme` for visual consistency.
    - Customizable text label for clear indication of the action.
    - Supports enabling/disabling the button based on conditions.
    - Can be styled further using the `modifier` parameter.

---

## Parameters

| Parameter      | Type                     | Description                                      |
|----------------|--------------------------|--------------------------------------------------|
| `onClick`      | `() -> Unit`             | Action to perform when the button is clicked.    |
| `modifier`     | `Modifier`               | Optional modifier for styling and positioning.   |
| `colors`       | `ButtonColors`           | Optional colors for the button. Defaults to primary color scheme. |
| `enabled`      | `Boolean`                | Whether the button is enabled or disabled. Defaults to true. |
| `text`         | `String`                 | Text to display on the button. Defaults to "Conferma". |

---

## Usage Example

```kotlin
ConfirmButton(
    onClick = { /* Handle confirmation action */ },
    modifier = Modifier.padding(16.dp),
    text = "Save Changes"
)
```

---

## Features

1. **Primary Color Scheme**:
    - Employs the primary color scheme from `MaterialTheme` for the button's background and text color, ensuring visual consistency with the application's design.

2. **Clear Label**:
    - Displays a user-friendly text label within the button, clearly indicating the action that will be performed upon clicking.

3. **Enabled/Disabled State**:
    - The `enabled` parameter controls the button's interactivity. When disabled, the button appears visually inactive and cannot be clicked.

4. **Customization**:
    - The `modifier` parameter allows for flexible styling and positioning of the button, such as padding, margins, and alignment.
    - The `colors` parameter enables customization of the button's appearance by overriding the default color scheme.

---

**Note:** The `ConfirmButton` component provides a solid foundation for implementing confirmation actions within your application. By leveraging its features and customization options, you can create a user-friendly and visually appealing experience.

---
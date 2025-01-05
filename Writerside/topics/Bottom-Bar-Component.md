# Bottom Bar [Component]

The `BottomBar` component provides a bottom navigation bar with items that allow users to switch between the main screens of the app. It highlights the selected screen and applies custom navigation actions for each item.

---

## Overview

- **Purpose**: Displays a bottom navigation bar with clickable items for easy navigation between key screens.
- **Key Features**:
    - Dynamically highlights the selected screen.
    - Provides haptic feedback for item interactions.
    - Supports smooth navigation to various app sections via `NavController`.

---

## Parameters

| Parameter       | Type                | Description                                                                             |
|-----------------|---------------------|-----------------------------------------------------------------------------------------|
| `navController` | `NavHostController` | Navigation controller to handle screen transitions between the items in the bottom bar. |

---

## Usage Example

```kotlin
BottomBar(
    navController = navController
)
```

---

## Features

1. **Navigation Items**:
    - Displays a set of navigation items (such as **Home** and **Requests**) with associated icons and labels.
    - Each item is linked to a screen and navigates the user to the corresponding route when clicked.

2. **Dynamic Item Highlighting**:
    - The label and icon of the currently selected item are highlighted with a bold font and distinct color to provide visual feedback to the user.

3. **Haptic Feedback**:
    - The component supports haptic feedback for each click, providing tactile confirmation of interactions.

4. **Custom Colors**:
    - The bottom bar and its items have customizable color settings for the text, icons, and background, matching the app's theme.

---

## Implementation Details

- **Dynamic Content**: The `BottomBar` dynamically generates a list of navigation items based on the `Screens` provided. Each item is represented by an icon and a label.
- **Navigation Logic**: The `navController` is used to navigate to the corresponding route when an item is clicked. The selected item is visually distinguished from the unselected ones.
- **Styling**: Items are styled with custom padding, background, and icon design for a polished user interface.
- **Typography**: The selected item label is made bold to emphasize the active screen, while unselected items use a normal font weight.

---

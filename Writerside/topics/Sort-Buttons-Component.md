# Sort Buttons [Component]

The `SortButtons` component is a UI element that displays a series of buttons, each representing a different sorting option. It allows the user to select a sort option, and it dynamically highlights the selected option.

---

## Overview

- **Purpose**: Displays a list of sort buttons, with one button highlighted to indicate the selected sort option.
- **Key Features**:
    - Dynamically generates buttons for each sort option.
    - Highlights the selected button using the primary color from the theme.
    - Includes a callback to handle button click events and update the selected sort option.

---

## Parameters

| Parameter              | Type                   | Description                                        |
|------------------------|------------------------|----------------------------------------------------|
| `sortOptions`          | `List<SortOption>`     | List of sort options to display as buttons.        |
| `selectedSortOption`   | `SortOption`           | The currently selected sort option.                |
| `onSortOptionSelected` | `(SortOption) -> Unit` | Callback to handle the selection of a sort option. |

---

## Usage Example

```kotlin
SortButtons(
    sortOptions = listOf(SortOption("Name"), SortOption("Date")),
    selectedSortOption = selectedOption,
    onSortOptionSelected = { selectedOption -> 
        // Handle sort option selection
    }
)
```

---

## Features

1. **Sort Option Buttons**:
    - Dynamically generates buttons for each sort option in the `sortOptions` list.
    - Each button is clickable and triggers the `onSortOptionSelected` callback.

2. **Button Styling**:
    - Buttons are styled with padding and color customization.
    - The selected button is highlighted using the primary color from the theme, while others maintain the default surface color.
    - The content color changes based on whether the button is selected or not.

3. **Responsive Design**:
    - The `Row` layout ensures that the buttons are displayed horizontally, adjusting to the screen width.

4. **Customization**:
    - You can further customize button styling through the `modifier` parameter to adjust its appearance and layout.

---

**Note**: The `SortOption` class should include a `label` property to represent the text shown on the button.

---

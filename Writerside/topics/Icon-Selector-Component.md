# Icon Selector [Component]

The `IconSelector` component is a reusable Composable designed to provide a dropdown menu for selecting an icon category. It uses Material Design principles and supports dynamic updates when an icon category is selected.

---

## Overview

- **Purpose**: Provides a dropdown menu to select an icon category.
- **Key Features**:
    - Displays a dropdown menu using `ExposedDropdownMenuBox`.
    - Allows for dynamic selection of `IconCategory` values.
    - Includes a leading icon and styled dropdown items.

---

## Parameters

| Parameter              | Type                     | Description                                  |
|------------------------|--------------------------|----------------------------------------------|
| `onIconSelected`       | `(IconCategory) -> Unit` | Callback to handle the selection of an icon. |
| `selectedIconCategory` | `IconCategory`           | The currently selected icon category.        |
| `modifier`             | `Modifier`               | Optional modifier for styling and layout.    |

---

## Usage Example

```kotlin
IconSelector(
    onIconSelected = { selectedCategory ->
        // Handle selected category
    },
    selectedIconCategory = IconCategory.OTHER,
    modifier = Modifier.padding(16.dp)
)
```

---

## Features

1. **Dropdown Menu**:
    - Displays available icon categories.
    - Closes automatically when a selection is made.

2. **Outlined TextField**:
    - Displays the current selection.
    - Includes a leading icon representing the selected category.

3. **Styling**:
    - Custom colors for focused and unfocused states.
    - Icons for both the dropdown menu and the leading icon in the text field.

4. **Customization**:
    - Fully customizable through the `modifier` parameter.

---

**Note**: The `iconCategoryMap` provides the mapping between `IconCategory` enum entries and their respective drawable resources.


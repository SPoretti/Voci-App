# Gender Selector [Component]

The `GenderSelector` component allows users to select a gender from a dropdown list. It displays the selected gender in a read-only text field and provides a dropdown menu for selecting a gender.

---

## Overview

- **Purpose**: Allows users to select a gender from a predefined list.
- **Key Features**:
    - Displays the selected gender in a read-only text field.
    - Provides a dropdown menu to choose from available gender options.
    - The dropdown menu can be toggled open and closed.
    - Ensures the user can only select from the predefined gender options.

---

## Parameters

| Parameter          | Type               | Description                                                   |
|--------------------|--------------------|---------------------------------------------------------------|
| `selectedGender`   | `Gender?`          | The currently selected gender, or `null` if none is selected. |
| `onGenderSelected` | `(Gender) -> Unit` | A callback function that is called when a gender is selected. |

---

## Usage Example

```kotlin
GenderSelector(
    selectedGender = selectedGender,
    onGenderSelected = { selectedGender -> /* handle gender selection */ }
)
```

---

## Features

1. **Gender Selection**:
    - Displays the currently selected gender or an empty string if none is selected.
    - Allows users to select a gender from a predefined list.
    - Uses an `ExposedDropdownMenu` to present the list of available gender options.

2. **Dropdown Menu**:
    - The gender options are displayed in a dropdown menu, which is shown when the user clicks on the text field.
    - The dropdown menu is toggleable by clicking on the text field or tapping outside the menu.

3. **Read-Only Text Field**:
    - The text field displaying the selected gender is read-only, preventing users from typing in the field directly.
    - The dropdown menu is the only way to change the selected gender.

4. **UI Customization**:
    - The dropdown menu and the text field are styled using Material 3 components.
    - The trailing icon in the text field changes based on whether the dropdown menu is open or closed.

5. **Feedback**:
    - The user receives visual feedback through the dropdown icon to indicate the open or closed state of the menu.

---

**Note**: The `Gender` enum should be defined to include the available gender options.

---

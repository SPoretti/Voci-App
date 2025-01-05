# Homeless List Item [Component]

The `HomelessListItem` component displays individual homeless item information in a list. It shows details such as name, status, location, and the ability to toggle a preferred icon.

---

## Overview

- **Purpose**: Displays a single item of the homeless list, including details such as name, status, age, and location. It includes interaction features like clicking to navigate to a profile and toggling the preferred status.
- **Key Features**:
    - Displays name, status, gender, age, and nationality.
    - Includes a location chip with a clickable map action.
    - Provides the option to toggle a "preferred" status for each homeless individual.
    - Supports user interaction for handling item clicks and chip clicks.

---

## Parameters

| Parameter           | Type                  | Description                                  |
|---------------------|-----------------------|----------------------------------------------|
| `homelessState`     | `HomelessItemUiState` | Data to display for the homeless individual. |
| `showPreferredIcon` | `Boolean`             | Whether to display the preferred icon.       |
| `onClick`           | `(Homeless) -> Unit`  | Callback to handle item click.               |
| `onChipClick`       | `() -> Unit`          | Callback to handle chip click for location.  |

---

## Usage Example

```kotlin
HomelessListItem(
    homelessState = homelessState,
    showPreferredIcon = true,
    onClick = { homeless -> /* Handle item click */ },
    onChipClick = { /* Handle chip click */ }
)
```

---

## Features

1. **Data Initialization**:
    - The component uses `HomelessItemUiState` to display detailed information about a homeless individual, including name, status, age, gender, nationality, and location.
    - The user's preferences are loaded via `volunteerViewModel`, allowing the component to track whether the homeless individual is marked as preferred.

2. **Preferred Status Toggle**:
    - The component includes an icon button that allows the user to toggle a "preferred" status for a specific homeless individual.
    - The icon changes between a filled star (`Icons.Filled.Star`) and an outlined star (`Icons.Filled.StarOutline`) based on whether the individual is preferred.
    - The `volunteerViewModel.toggleHomelessPreference` method is used to update the preferred status.

3. **Location Chip**:
    - The `CustomChip` is used to display the location of the homeless individual. Clicking the chip triggers the `onChipClick` callback, which could navigate to a map showing the individual's location.

4. **Status LED**:
    - A `StatusLED` is used to visually represent the status of the homeless individual. The color and pulsation of the LED vary depending on the individual's status (`GREEN`, `YELLOW`, `RED`, `GRAY`).

5. **Layout and Styling**:
    - The layout is a `Surface` containing a `Row`, which houses a `Column` for textual information (name, age, nationality) and a `Box` for the location chip and preferred icon.
    - The `Surface` component is styled with rounded corners and a background color matching the app’s theme.

---

## Known Limitations

- **Icon Customization**: The preferred icon is a star, and its appearance is limited to two states (filled and outlined). Future versions could allow for different icons or more customization.
- **Fixed Layout**: The `height` of the `Surface` is fixed at 75 dp, which might not be ideal for all screen sizes. Making it dynamic could improve responsiveness.

---

## Notes

- **Dependencies**:
    - Requires the `HomelessItemUiState` to be passed in as a parameter for displaying specific homeless individual data.
    - Uses `volunteerViewModel` to manage and toggle the preferred homeless individuals.

- **Future Improvements**:
    - Add more interactivity, such as a pop-up for additional details when clicking on a list item.
    - Consider adding animations when the preferred status is toggled to improve the user experience.


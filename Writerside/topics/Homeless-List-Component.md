# Homeless List [Component]

The `HomelessList` component is a part of the home screen that displays a list of homeless individuals. It allows for filtering and search functionality and supports swipe gestures to navigate to detailed views or add updates.

---

## Overview

- **Purpose**: Displays a list of homeless individuals, with the ability to search and filter results. It enables swipe gestures for easy navigation and adding updates.
- **Key Features**:
    - Displays a list of homeless individuals with search and filtering capabilities.
    - Allows swipe gestures to navigate to a detailed view or add updates.
    - Integrates with view models to fetch and manage data.

---

## Parameters

| Parameter       | Type            | Description                                           |
|-----------------|-----------------|-------------------------------------------------------|
| `navController` | `NavController` | Navigation controller for navigating between screens. |

---

## Usage Example

```kotlin
HomelessList(
    navController = navController
)
```

---

## Features

1. **Data Initialization**:
    - The component initializes the `homelessViewModel` and `volunteerViewModel` from the `serviceLocator` to fetch the required data.
    - The list of homeless individuals is fetched from the `homelessViewModel.homelesses`, and filtered results are accessed from `filteredHomelesses`.
    - The `searchQuery` is used to filter the list of homeless individuals.

2. **Swipe Gesture Interaction**:
    - The component uses `SwipeToDismissBox` to allow users to swipe an item from left to right to navigate to the "UpdatesAddScreen" and add updates for a specific homeless individual.
    - The background content revealed during the swipe shows a comment icon and the text "Aggiornamento."

3. **Resource State Management**:
    - The component handles different states of the data (`Loading`, `Success`, `Error`):
        - **Loading**: Displays a loading indicator.
        - **Success**: Renders a list of homeless individuals, sorted by their preference if they are marked as favorites.
        - **Error**: Displays an error message if data fetch fails.

4. **User Interaction**:
    - Tapping on an individual item in the list navigates to a detailed profile of the homeless individual.
    - A floating action button can be used to filter or perform actions on the list, if necessary.

5. **Sorting**:
    - The list of homeless individuals is sorted by favorited status, with preferred individuals displayed at the top of the list.

---

## Known Limitations

- **Performance with Large Data Sets**: If the number of homeless individuals is very large, the swipe-to-dismiss interaction and the sorting logic may cause performance issues.
- **Error Handling**: The error message is displayed generically; specific error details could be added for better troubleshooting.

---

## Notes

- **Dependencies**:
    - Relies on the `HomelessViewModel` and `VolunteerViewModel` to fetch and manage the data.
    - Uses the `NavController` for navigation to detailed screens and updates.

- **Future Improvements**:
    - Add more complex filtering options to allow users to filter by other criteria such as age, location, etc.
    - Implement a loading retry mechanism when data fetch fails.


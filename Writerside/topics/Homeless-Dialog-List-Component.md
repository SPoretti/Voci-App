# Homeless Dialog List [Component]

The `HomelessDialogList` component displays a list of homeless individuals in a dialog format, allowing users to select an individual to link to a request. This component is similar to `HomelessList`, but specifically designed for use in a dialog, where selection is the key functionality.

---

## Overview

- **Purpose**: Displays a list of homeless individuals that users can select from to associate with a request. The list is searchable, and individuals can be selected by clicking on their entry.
- **Key Features**:
    - Displays a list of homeless individuals with a search bar to filter the results.
    - Allows selection of an individual by clicking on their item.
    - Handles loading, success, and error states to ensure smooth user experience.

---

## Parameters

| Parameter         | Type                 | Description                                                |
|-------------------|----------------------|------------------------------------------------------------|
| `onListItemClick` | `(Homeless) -> Unit` | Callback to handle item selection.                         |
| `navController`   | `NavController`      | Navigation controller used for handling navigation events. |

---

## Usage Example

```kotlin
HomelessDialogList(
    onListItemClick = { homeless -> /* Handle selection */ },
    navController = navController
)
```

## Features

1. **Data Initialization**:
    - The component fetches the list of homeless individuals from `homelessViewModel.homelesses`.
    - The list is filtered based on the search query entered by the user (`searchQuery`), and the results are displayed accordingly.

2. **Loading, Success, and Error States**:
    - The component handles different resource states: loading, success, and error.
    - A `CircularProgressIndicator` is shown while the data is loading.
    - Once the data is successfully fetched, the list is displayed. If an error occurs during the data fetch, an error message is shown along with a button to go back.

3. **Search and Filter**:
    - The component allows the user to search for homeless individuals by entering a query. The search is handled by filtering the `homelesses` list using `filteredHomelesses`.

4. **Item Selection**:
    - Each item in the list is represented by the `HomelessListItem` component. When clicked, the item triggers the `onListItemClick` callback, allowing the user to select a homeless individual.
    - The `HomelessListItem` is used to display each individual, but the `showPreferredIcon` is set to `false` in this component since it is not used for marking preferences.

5. **Layout and Styling**:
    - The list is displayed inside a `LazyVerticalGrid` that ensures a grid layout. This is useful for maintaining a clean and responsive design.
    - The items are displayed one by one in a grid with each item's view wrapped in a card-like `Surface`.

---

## Known Limitations

- **Performance with Large Lists**: While the component uses `LazyVerticalGrid` to efficiently display the list, performance may degrade with an extremely large list of homeless individuals. Optimizations may be required for very large data sets.
- **Error Handling**: The current error handling simply shows a text message and a "Go back" button. More advanced error handling, such as retry mechanisms, could improve the user experience.

---

## Notes

- **Dependencies**:
    - Requires `HomelessItemUiState` to display the information for each homeless individual.
    - Uses `homelessViewModel` and `volunteerViewModel` for managing data and preferences.

- **Future Improvements**:
    - Add more detailed error handling or retry functionality.
    - Consider adding additional features, such as sorting or grouping homeless individuals by different attributes.

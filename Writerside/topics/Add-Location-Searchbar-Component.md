# AddLocationSearchbar [Component]

The `AddLocationSearchbar` component provides a dynamic and interactive search bar for finding and selecting locations. It integrates seamlessly with a ViewModel for fetching location suggestions and handles user interactions like clearing search input and selecting suggestions.

---

## Overview

- **Purpose**: Allows users to search for and select locations interactively.
- **Key Features**:
    - Real-time location suggestions.
    - Clearable search input field.
    - Displays a list of suggestions with clickable items.
    - Supports current location proximity for refined search results.

---

## Parameters

| Parameter  | Type               | Description                                             |
|------------|--------------------|---------------------------------------------------------|
| `onClick`  | `(String) -> Unit` | Callback function executed when a location is selected. |
| `modifier` | `Modifier`         | (Optional) Modifier to customize the layout appearance. |

---

## Usage Example

```kotlin
AddLocationSearchbar(
    onClick = { location ->
        Log.d("Selected Location", location)
    },
    modifier = Modifier.fillMaxWidth()
)
```

---

## Features

1. **Dynamic Search Input**:
    - Updates the suggestion list as the user types.
    - Clears the input field with a trailing icon button.

2. **Location Suggestions**:
    - Fetches real-time suggestions based on the input text.
    - Uses the device's current location for proximity-based suggestions.

3. **Interactive UI**:
    - Suggestion items are clickable and update the search field upon selection.

4. **Integration with ViewModel**:
    - Fetches location suggestions using `homelessViewModel.fetchSuggestions()`.
    - Uses `sessionToken` for session-based query tracking.

5. **Customizable Design**:
    - Accepts a `modifier` parameter for flexible layout adjustments.

---

## Known Limitations

- **Error Handling**: No explicit UI for displaying errors during suggestion fetching.
- **Current Location Dependency**: Relies on `LocationHandler` to provide current location for proximity.
- **Limited Address Handling**: Displays only `full_address` or `name` if available.

---

## Notes

- **Dependencies**:
    - Requires integration with a ViewModel that provides `suggestedLocations` and `fetchSuggestions` methods.
    - Uses `LocationHandler` for accessing current location.

- **Future Improvements**:
    - Add error messages for failed suggestion fetching.
    - Enhance address formatting for better user readability.
    - Improve UX by adding loading indicators during fetch operations.

---

## References

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Location Services API](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary)
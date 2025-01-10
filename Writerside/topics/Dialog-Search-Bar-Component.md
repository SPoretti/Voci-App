# Dialog Search Bar [Component]

## Purpose

`DialogSearchBar` is a lightweight, reusable composable designed for search functionality within dialog-based interfaces. It is compact and modular, providing a simple search input for filtering or querying data.

## Parameters

- `onSearch: (String) -> Unit`: A callback invoked with the updated search query whenever the user types in the search bar.

## Features

1. **Dynamic Search Updates**:
    - The search query (`searchText`) is dynamically updated on user input.
    - Invokes the `onSearch` callback with the updated query for real-time filtering or processing.

2. **Customizable UI**:
    - Styled with Material Design principles for seamless integration with modern Android UIs.
    - Includes placeholder text (`"Cerca un senzatetto..."`) for clarity and usability.

3. **Responsive Icon**:
    - Leading search icon for intuitive user interaction, styled to blend with the app's color scheme.

## Usage Example

```kotlin
DialogSearchBar(
    onSearch = { query ->
        // Perform search or update UI with the query
        println("Search query: $query")
    }
)
```

---

**Note**: This `DialogSearchBar` was originally part of the same file as another `SearchBar` component. To improve modularity and readability, it was separated into its own file. This change ensures that each composable serves a focused purpose and reduces the complexity of maintaining shared code.

# Search Bar [Component]

## Overview

The `SearchBar` component is a dynamic and reusable input field designed for user-friendly search and navigation. It includes customizable leading and trailing icons to enhance interactivity.

## Features

- **Search Input**:
    - Users can enter a search query.
    - Updates a `HomelessViewModel` search query dynamically.

- **Leading Icon**:
    - A customizable icon (default: `Dehaze`) for opening drawers or triggering other actions.

- **Trailing Icon**:
    - Clears the search query (`Close` icon) if text is present.
    - Displays the user's profile picture or a default icon for navigation to a profile screen.

## Props

| Prop                 | Type            | Description                                                   |
|----------------------|-----------------|---------------------------------------------------------------|
| `navController`      | `NavController` | The navigation controller for handling navigation actions.    |
| `onLeadingIconClick` | `() -> Unit`    | Callback function triggered when the leading icon is clicked. |

## Behavior

### Search Query
- The search field listens for changes and dynamically updates the `HomelessViewModel`.
- Clearing the field resets the query and refreshes the view.

### Icons
- **Leading Icon**:
    - Opens a navigation drawer or performs a custom action.
- **Trailing Icon**:
    - Displays context-sensitive options:
        - Clear the search field if it contains text.
        - Navigate to the user profile screen, showing either a profile picture or a default user icon.

### Styling
- Rounded rectangle for the input field.
- Colors adjust for both focused and unfocused states.
- Trailing icons adapt based on user login status and profile picture availability.

## Usage Example

Include the `SearchBar` in a screen or component as follows:

```kotlin
SearchBar(
    navController = navController,
    onLeadingIconClick = { /* Custom drawer or action logic */ }
)
```
This setup dynamically binds the `SearchBar` to the navigation controller and drawer action.

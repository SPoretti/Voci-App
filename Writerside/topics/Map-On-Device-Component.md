# Map On Device [Component]

The `MapOnDevice` component is a Mapbox-based map view that tracks and displays the user's current location. It allows users to add new locations by interacting with a floating action button (FAB) on the screen. The map updates the camera position to follow the user's location.

---

## Overview

- **Purpose**: Displays the user's current location on a map and provides a button to add a new location.
- **Key Features**:
    - Tracks user location in real-time.
    - Updates the camera to follow the user's position.
    - Allows the user to add a new location via a floating action button.

---

## Parameters

| Parameter          | Type              | Description                                               |
|--------------------|-------------------|-----------------------------------------------------------|
| `onAddNewLocation` | `(Point) -> Unit` | Callback function triggered when a new location is added. |

---

## Usage Example

```kotlin
MapOnDevice(
    onAddNewLocation = { point ->
        // Handle new location logic here
    }
)
```

---

## Features

1. **Location Tracking**:
    - Uses the Mapbox `locationComponentPlugin` to track the user's location and updates it in real-time.
    - The current location is stored in a mutable state variable `location`.

2. **Camera Follows Location**:
    - Configures the map to follow the user's location by transitioning the camera to the "follow puck" state.
    - The map's camera position, zoom, and other settings are dynamically updated based on the user's location.

3. **Floating Action Button**:
    - A floating action button (FAB) labeled "Add Location" allows the user to save the current location when clicked.
    - The FAB is positioned at the bottom-right corner of the screen and includes a location icon.

4. **Custom Map Styling**:
    - Configures the map with a default 2D puck bearing (indicating the user's heading).
    - The location puck updates its bearing based on the device's heading.

---

## Known Limitations

- **Initial Location**: The initial location is set to (0.0, 0.0) if no location is available when the button is clicked.
- **Camera Transition**: The camera may not always follow the puck smoothly on some devices due to performance limitations.

---

## Notes

- **Dependencies**:
    - Requires Mapbox SDK setup, including API keys and permissions.
    - Requires a `CustomFAB` composable to handle the floating action button.

- **Future Improvements**:
    - Add location validation and error handling for invalid locations.
    - Support for different map styles based on the user's preference.

---

## References

- [Mapbox Documentation](https://docs.mapbox.com/)
    - [Location Component](https://docs.mapbox.com/android/maps/guides/location/)
    - [Map Camera](https://docs.mapbox.com/android/maps/guides/camera/)
    - [Map Effects](https://docs.mapbox.com/android/maps/guides/animation/)

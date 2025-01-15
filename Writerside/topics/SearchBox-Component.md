# Search Box [Component]

The `SearchBox` component is a user interface element that allows users to search for a location, view it on a map, and confirm their selection. It combines a search bar, a map view, and buttons for selecting the current or searched location.

---

## Overview

- **Purpose**: Provides a seamless way for users to search and select locations on a map.
- **Key Features**:
    - Integrated search bar with geocoding support.
    - Displays searched or current location on a map.
    - Enables users to confirm a selected location.

---

## Parameters

| Parameter           | Type               | Description                                               |
|---------------------|--------------------|-----------------------------------------------------------|
| `onConfirmLocation` | `(String) -> Unit` | Callback function triggered when a location is confirmed. |

---

## Usage Example

```kotlin
SearchBox(
    onConfirmLocation = { location ->
        // Handle confirmed location here
        println("Selected Location: $location")
    }
)
```

---

## Features

1. **Interactive Map Integration**:
    - Utilizes `MultiPointMap` to display the searched or current location.
    - Supports smooth camera animations to focus on the selected location.

2. **Search Functionality**:
    - Includes a search bar for forward geocoding.
    - Fetches the address for coordinates using reverse geocoding.

3. **Current Location Handling**:
    - Uses `LocationHandler` to fetch the device's current location.
    - Updates the map view based on the current location.

4. **Confirmation Workflow**:
    - Allows users to confirm their selected location via a dialog.
    - Provides visual feedback for the selected address.

5. **Offline Handling**:
    - Displays errors or fallback messages when location services are unavailable.

---

## Known Limitations

- **Dependent on Network Connectivity**:
    - Requires internet access for geocoding and map rendering.
- **Single Location Selection**:
    - Only supports one selected location at a time.

---

## Notes

- **Dependencies**:
    - Requires `MultiPointMap` and `AddLocationSearchbar` components for map rendering and search functionality.
    - Relies on `LocationHandler` for accessing the device’s current location.
    - Utilizes `ServiceLocator` to obtain the `HomelessViewModel`.

- **Future Improvements**:
    - Add support for multiple location selections.
    - Enhance offline functionality with cached map data.
    - Improve error handling for geocoding failures.

---

## References

- [Mapbox Documentation](https://docs.mapbox.com/)
    - [Forward Geocoding](https://docs.mapbox.com/api/search/geocoding/#forward-geocoding)
    - [Reverse Geocoding](https://docs.mapbox.com/api/search/geocoding/#reverse-geocoding)


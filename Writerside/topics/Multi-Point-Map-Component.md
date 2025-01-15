# Multi Point Map [Component]

The `MultiPointMap` component is a Mapbox-based map view that displays multiple points of interest using custom markers. It provides smooth camera animations and allows users to interact with the map.

---

## Overview

- **Purpose**: Displays a customizable map with multiple points and smooth camera transitions.
- **Key Features**:
    - Renders points of interest with markers.
    - Supports smooth animations for camera positioning.
    - Fully interactive with zoom, pan, and tilt capabilities.

---

## Parameters

| Parameter       | Type            | Description                                        |
|-----------------|-----------------|----------------------------------------------------|
| `points`        | `List<Point>`   | List of geographical points to display as markers. |
| `cameraOptions` | `CameraOptions` | Defines the initial camera position and settings.  |

---

## Usage Example

```kotlin
MultiPointMap(
    points = listOf(Point.fromLngLat(-77.0369, 38.9072)),
    cameraOptions = CameraOptions.Builder()
        .center(Point.fromLngLat(-77.0369, 38.9072))
        .zoom(12.0)
        .build()
)
```

---

## Features

1. **Dynamic Point Rendering**:
    - Iterates through the `points` list to add markers for each location on the map.
    - Uses `rememberIconImage` for custom marker icons.

2. **Smooth Camera Animations**:
    - Configures camera position, zoom, pitch, and bearing with `easeTo` for smooth transitions.
    - Supports animation customization through `MapAnimationOptions`.

3. **Interactive Map**:
    - Leverages Mapbox’s built-in gestures for zooming, panning, and tilting.

4. **Customization**:
    - Flexible camera settings to define the map’s initial view.
    - Custom marker icons using drawable resources.

5. **Theme Support**:
    - Dynamically switches between dark and light themes based on system settings.

6. **Offline Handling**:
    - Displays a placeholder message when the device is offline.

---

## Known Limitations

- **Static Marker Icons**: Currently, all points use the same marker icon (`R.drawable.marker_icon`).
- **Animation Dependency**: Transitions rely on `MapAnimationOptions`, which may not suit all scenarios.
- **Offline Behavior**: Limited functionality when the network is unavailable.

---

## Notes

- **Dependencies**:
    - Requires Mapbox SDK setup, including API keys and permissions.
    - Custom marker icon must be added to the `drawable` resources.

- **Future Improvements**:
    - Implement clustering for handling large datasets.
    - Enable dynamic marker customization based on point attributes.
    - Add error handling for invalid input or camera locations.

---

## References

- [Mapbox Documentation](https://docs.mapbox.com/)
    - [Point Annotation](https://docs.mapbox.com/android/maps/guides/annotations/)
    - [Map Camera Options](https://docs.mapbox.com/android/maps/guides/camera/)
    - [Map Animation Options](https://docs.mapbox.com/android/maps/guides/animation/)


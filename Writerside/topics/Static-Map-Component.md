# Static Map [Component]

The `StaticMap` component displays a static map image centered at a specific geographical location using the Mapbox Static Images API. It also provides a clickable interface to navigate to the location using Google Maps or other compatible mapping applications.

---

## Overview

- **Purpose**: Displays a static map image for a specific location and provides navigation functionality.
- **Key Features**:
    - Generates a static map image from the Mapbox Static Images API.
    - Provides a clickable interface to launch navigation apps for the specified location.

---

## Parameters

| Parameter   | Type     | Description                  |
|-------------|----------|------------------------------|
| `latitude`  | `Double` | Latitude of the map center.  |
| `longitude` | `Double` | Longitude of the map center. |

---

## Usage Example

```kotlin
StaticMap(
    latitude = 45.4642,
    longitude = 9.19
)
```

---

## Features

1. **Static Map Generation**:
    - Uses the Mapbox Static Images API to generate a map centered at the specified latitude and longitude.
    - Displays a marker on the map using the pin style format (`pin-s+ff0000`).

2. **Navigation Integration**:
    - Clicking the map opens a navigation app to direct the user to the specified location.
    - Supports Google Maps and provides a fallback for other mapping applications if Google Maps is not installed.

3. **Responsive Design**:
    - Ensures the map image scales correctly using `Modifier.fillMaxWidth()` and a fixed height.

4. **Error Handling**:
    - Gracefully handles scenarios where Google Maps is not installed by providing alternative navigation options.

---

## Notes

- **Dependencies**:
    - Requires an active internet connection to fetch the map image from the Mapbox API.
    - Ensure the Mapbox API key (`access_token`) is configured correctly.

- **Customization**:
    - The marker style and zoom level can be adjusted by modifying the API URL.
    - Default zoom level is set to `14`, and the image size is `600x300` pixels.

- **Limitations**:
    - The map is static and does not support gestures like zoom or pan.
    - Requires a valid API key for Mapbox Static Images API.

---

## Future Improvements

- Allow dynamic configuration of marker styles and zoom levels.
- Add support for offline fallback maps.
- Enhance accessibility by providing alternative text descriptions for the map image.

---

## References

- [Mapbox Static Images API](https://docs.mapbox.com/api/maps/static-images/)
- [Google Maps Intent Documentation](https://developer.android.com/guide/components/intents-common#Maps)


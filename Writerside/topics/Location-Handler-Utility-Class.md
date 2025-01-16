# LocationHandler [Utility Class]

The `LocationHandler` is a utility class that facilitates fetching the user's current location by utilizing the `FusedLocationProviderClient`. It ensures proper permissions are checked and provides fallbacks for fetching the most recent or live location when needed.

---

## Overview

- **Purpose**: Retrieves the user's current geographical coordinates.
- **Key Features**:
    - Checks for necessary location permissions.
    - Retrieves the last known location or fetches the live location.
    - Handles errors gracefully and logs relevant information.

---

## Parameters

The `LocationHandler` class is instantiated via dependency injection and requires the following parameters:

| Parameter             | Type                          | Description                                |
|-----------------------|-------------------------------|--------------------------------------------|
| `context`             | `Context`                     | Application context for location services. |
| `fusedLocationClient` | `FusedLocationProviderClient` | Client for accessing location services.    |

---

## Methods

### getCurrentLocation

Suspending function to fetch the current location.

#### Declaration:
```kotlin
suspend fun getCurrentLocation(callback: (Pair<Double, Double>?) -> Unit)
```

#### Description:
Fetches the current location using the following steps:

1. Checks if location permissions (`ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION`) are granted.
2. Attempts to retrieve the last known location.
3. If the last known location is unavailable, tries to fetch the live current location.
4. Invokes the callback function with the coordinates (`latitude`, `longitude`) or `null` if an error occurs.

#### Parameter:
| Parameter  | Type                              | Description                                                              |
|------------|-----------------------------------|--------------------------------------------------------------------------|
| `callback` | `(Pair<Double, Double>?) -> Unit` | A function to handle the retrieved coordinates or `null` if unavailable. |

#### Usage Example:
```kotlin
val locationHandler = LocationHandler(context, fusedLocationClient)
locationHandler.getCurrentLocation { location ->
    if (location != null) {
        val (latitude, longitude) = location
        Log.d("MainActivity", "Current location: $latitude, $longitude")
    } else {
        Log.d("MainActivity", "Unable to fetch location.")
    }
}
```

---

## Features

1. **Permission Handling**:
    - Ensures that necessary permissions are granted before accessing location services.

2. **Fallback Mechanism**:
    - Retrieves the last known location as a first step.
    - If the last location is unavailable, fetches the live current location.

3. **Error Logging**:
    - Logs detailed messages for various error scenarios, aiding in debugging.

---

## Known Limitations

- **Permission Dependency**: Requires proper handling of runtime location permissions by the calling component.
- **Accuracy**: Relies on the accuracy of the `FusedLocationProviderClient` for location data.
- **Context-Specific**: The `context` parameter must be valid and tied to the lifecycle of the application.

---

## Notes

- **Dependencies**:
    - Requires the Android `FusedLocationProviderClient` setup.
    - Permissions for `ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION` must be declared and handled.

- **Future Improvements**:
    - Add support for location updates over time.
    - Implement fallback mechanisms for additional location providers.

---

## References

- [Fused Location Provider Client Documentation](https://developer.android.com/training/location)
- [Location Permissions](https://developer.android.com/training/location/permissions)
- [Google Play Services Location API](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient)


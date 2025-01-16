# Mapbox [Viewmodel]

## Overview

`MapboxViewModel` is responsible for handling all interactions with the Mapbox API for geocoding, reverse geocoding, and location suggestions. This ViewModel uses the `MapboxRepository` for backend communication and manages state variables to hold results and provide updates to the UI.

## State Variables

### Location Coordinates
- **Variable:** `_locationCoordinates`
- **Type:** `MutableStateFlow<Resource<Pair<Double, Double>>>`
- **Description:** Holds the geocoded latitude and longitude pair for a given address.
- **Public Access:** `locationCoordinates`

### Location Address
- **Variable:** `_locationAddress`
- **Type:** `MutableStateFlow<Resource<String>>`
- **Description:** Holds the reverse-geocoded address for a given set of latitude and longitude.
- **Public Access:** `locationAddress`

### Suggested Locations
- **Variable:** `_suggestedLocations`
- **Type:** `MutableStateFlow<Resource<List<Suggestion>>>`
- **Description:** Holds a list of suggested locations based on user input.
- **Public Access:** `suggestedLocations`

## Methods

### fetchSuggestions
```kotlin
fun fetchSuggestions(
    query: String,
    sessionToken: String,
    proximity: String? = null
)
```
Fetches location suggestions based on the user's query.

- **Parameters:**
    - `query` *(String)*: The search query string.
    - `sessionToken` *(String)*: A token to track the search session.
    - `proximity` *(String?)*: Optional proximity coordinates to prioritize nearby results.
- **State Updates:**
    - Updates `_suggestedLocations` with the result or error.

### forwardGeocoding
```kotlin
fun forwardGeocoding(query: String, proximity: String? = null)
```
Performs forward geocoding to fetch coordinates for a given address.

- **Parameters:**
    - `query` *(String)*: The address to geocode.
    - `proximity` *(String?)*: Optional proximity coordinates.
- **State Updates:**
    - Updates `_locationCoordinates` with the geocoded coordinates or error.

### reverseGeocoding
```kotlin
fun reverseGeocoding(latitude: Double, longitude: Double)
```
Performs reverse geocoding to fetch an address for a given set of coordinates.

- **Parameters:**
    - `latitude` *(Double)*: Latitude of the location.
    - `longitude` *(Double)*: Longitude of the location.
- **State Updates:**
    - Updates `_locationAddress` with the reverse-geocoded address or error.

### forwardGeocodingAsync
```kotlin
suspend fun forwardGeocodingAsync(query: String): Pair<Double, Double>?
```
Asynchronously performs forward geocoding to fetch coordinates for a given address.

- **Parameters:**
    - `query` *(String)*: The address to geocode.
- **Returns:**
    - A `Pair` of `latitude` and `longitude` if successful, or `null` if an error occurs.

### clearLocationVariables
```kotlin
fun clearLocationVariables()
```
Clears the state variables for location coordinates and address.

- **State Updates:**
    - Resets `_locationCoordinates` and `_locationAddress` to `Resource.Loading()`.

## Dependencies

- **`MapboxRepository`**: Provides the methods `getMapboxSuggestions`, `geocodeAddress`, and `reverseGeocode` to interact with the Mapbox API.


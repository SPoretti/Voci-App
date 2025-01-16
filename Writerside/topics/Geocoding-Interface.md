# Geocoding [Interface]

The `GeocodingInterface` defines API endpoints for interacting with the Mapbox Geocoding API. It provides functions for both forward and reverse geocoding requests.

## Forward Geocoding

This function sends a request to the Mapbox Geocoding API to get coordinates for a given address or location name.

### Forward: Function Definition

```kotlin
@GET("search/geocode/v6/forward")
suspend fun geocodeAddress(
    @Query("q") query: String,
    @Query("access_token") accessToken: String,
    @Query("language") language: String? = null,
    @Query("proximity") proximity: String? = null,
    @Query("country") country: String? = null,
    @Query("types") types: String? = null,
    @Query("limit") limit: Int? = null,
    @Query("autocomplete") autocomplete: Boolean? = null,
    @Query("bbox") bbox: String? = null,
    @Query("worldview") worldview: String? = null,
    @Query("format") format: String? = null,
    @Query("permanent") permanent: Boolean? = null
): GeocodingResponse
```

### Forward:  Parameters

- `query`: Address or location name to geocode.
- `accessToken`: Access token for authenticating with the Mapbox API.
- `language`: Language of the results (optional).
- `proximity`: Coordinates to prioritize results near (optional).
- `country`: Comma-separated list of country codes to restrict results (optional).
- `types`: Comma-separated list of result types (optional).
- `limit`: Maximum number of results to return (optional).
- `autocomplete`: Enable or disable autocomplete (optional).
- `bbox`: Bounding box to restrict search area (optional).
- `worldview`: Worldview parameter to customize result set (optional).
- `format`: Format of the results, e.g., "geojson" (optional).
- `permanent`: Whether the geocoding results are permanent (optional).

### Forward:  Return Value

A `GeocodingResponse` object containing the results of the forward geocoding request.

---

## Reverse Geocoding

This function sends a request to the Mapbox Geocoding API to get an address or location name for given coordinates.

### Reverse: Function Definition

```kotlin
@GET("search/geocode/v6/reverse")
suspend fun reverseGeocode(
    @Query("longitude") longitude: String,
    @Query("latitude") latitude: String,
    @Query("access_token") accessToken: String,
    @Query("language") language: String? = null,
    @Query("types") types: String? = null,
    @Query("limit") limit: Int? = null,
    @Query("worldview") worldview: String? = null,
    @Query("permanent") permanent: Boolean? = null
): GeocodingResponse
```

### Reverse:  Parameters

- `longitude`: Longitude coordinate of the location.
- `latitude`: Latitude coordinate of the location.
- `accessToken`: Access token for authenticating with the Mapbox API.
- `language`: Language of the results (optional).
- `types`: Comma-separated list of result types (optional).
- `limit`: Maximum number of results to return (optional).
- `worldview`: Worldview parameter to customize result set (optional).
- `permanent`: Whether the geocoding results are permanent (optional).

### Reverse:  Return Value

A `GeocodingResponse` object containing the results of the reverse geocoding request.

---

## Notes

- **Dependencies:** This interface relies on Retrofit to handle HTTP requests and responses.
- **Authentication:** A valid Mapbox access token must be provided for all API calls.
- **Error Handling:** Ensure proper error handling is implemented for network issues and API errors.

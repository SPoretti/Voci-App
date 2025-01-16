# Mapbox [Repository]

The `MapboxRepository` class serves as a centralized repository for interacting with Mapbox APIs, including Search and Geocoding functionalities. It abstracts the API calls and handles the interactions with the `SearchInterface` and `GeocodingInterface` provided by `RetrofitFactory`.

---

## Details

### Properties

- **`searchApi`**: Instance of `SearchInterface`, created using `RetrofitFactory`. Used for Search API requests.
- **`geocodingApi`**: Instance of `GeocodingInterface`, created using `RetrofitFactory`. Used for Geocoding API requests.

### Functions

#### `getMapboxSuggestions`
Fetches location suggestions based on the provided query and other optional parameters.

- **Parameters:**
    - `query`: The search query string.
    - `accessToken`: Mapbox API access token.
    - `sessionToken`: Token to maintain session consistency.
    - Additional optional parameters for language, proximity, bounding box, etc.
- **Returns:** `SuggestionResponse`

#### `geocodeAddress`
Performs forward geocoding to retrieve coordinates from an address.

- **Parameters:**
    - `query`: The address or location query.
    - `accessToken`: Mapbox API access token.
    - Additional optional parameters for language, bounding box, etc.
- **Returns:** `GeocodingResponse`

#### `reverseGeocode`
Performs reverse geocoding to retrieve address information from latitude and longitude.

- **Parameters:**
    - `longitude`: Longitude of the location.
    - `latitude`: Latitude of the location.
    - `accessToken`: Mapbox API access token.
    - Additional optional parameters for language, types, etc.
- **Returns:** `GeocodingResponse`

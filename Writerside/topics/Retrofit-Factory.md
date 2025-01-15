# Retrofit [Factory]

### Overview
`RetrofitFactory` is an object that serves as a centralized factory for creating Retrofit instances to interact with the Mapbox APIs. It ensures that the necessary configurations, such as base URL, HTTP client, and logging interceptors, are consistently applied across different API interfaces.

---

### Constants

#### `BASE_URL`
- **Type:** `String`
- **Value:** `"https://api.mapbox.com/"`
- **Description:** The base URL for accessing the Mapbox API.

---

### Components

#### Logging Interceptor
- **Type:** `HttpLoggingInterceptor`
- **Configuration:**
    - Level: `HttpLoggingInterceptor.Level.BODY`
- **Purpose:** Captures and logs HTTP request and response details for debugging purposes.

#### OkHttp Client
- **Type:** `OkHttpClient`
- **Description:** Configured with the logging interceptor to enable detailed logging of HTTP interactions.

---

### API Instances

#### `searchApi`
- **Type:** `SearchInterface`
- **Description:** A Retrofit instance configured for interacting with the Mapbox Search API.
- **Initialization:**
    - Base URL: `BASE_URL`
    - HTTP Client: `okHttpClient`
    - Converter Factory: `GsonConverterFactory`

#### `geocodingApi`
- **Type:** `GeocodingInterface`
- **Description:** A Retrofit instance configured for interacting with the Mapbox Geocoding API.
- **Initialization:**
    - Base URL: `BASE_URL`
    - HTTP Client: `okHttpClient`
    - Converter Factory: `GsonConverterFactory`

---

### Usage
To use the factory, access the desired API instance as follows:

```kotlin
val searchService = RetrofitFactory.searchApi
val geocodingService = RetrofitFactory.geocodingApi
```

These instances can then be used to perform API requests defined in their respective interface contracts.

---

### Advantages
- **Centralized Configuration:** Ensures uniform configurations for all Retrofit instances.
- **Lazy Initialization:** Optimizes resource usage by initializing only when accessed.
- **Debugging Support:** Logging interceptor facilitates easier debugging of HTTP requests and responses.

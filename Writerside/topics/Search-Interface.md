# Search [Interface]

### SearchInterface

The `SearchInterface` defines the API endpoints for interacting with the Mapbox Search API. It includes a method for fetching location suggestions based on a search query.

#### Method: `getSuggestions`
Fetches location suggestions based on the provided query and additional parameters.

**Endpoint:** `GET /search/searchbox/v1/suggest`

##### Parameters:
- `query` (`String`): The search query text.
- `accessToken` (`String`): Mapbox API access token.
- `sessionToken` (`String`): A unique token for the search session.
- `language` (`String?`, optional): The language for the response.
- `limit` (`Int?`, optional): Maximum number of suggestions to return.
- `proximity` (`String?`, optional): Coordinates to prioritize suggestions (e.g., `longitude,latitude`).
- `bbox` (`String?`, optional): Bounding box to limit search results (e.g., `minLon,minLat,maxLon,maxLat`).
- `country` (`String?`, optional): Filter results to specific countries (ISO 3166-1 alpha-2 codes).
- `types` (`String?`, optional): Filter results by place types (e.g., `place`, `neighborhood`).
- `poiCategory` (`String?`, optional): Filter results by points of interest categories.
- `poiCategoryExclusions` (`String?`, optional): Exclude specific points of interest categories.
- `etaType` (`String?`, optional): Specify the ETA type for the search.
- `navigationProfile` (`String?`, optional): Navigation profile to use.
- `origin` (`String?`, optional): Specify the origin coordinates.

##### Returns:
- `SuggestionResponse`: A data class containing the list of location suggestions and associated metadata.

#### Example Usage:
```kotlin
val response = searchApi.getSuggestions(
    query = "coffee shop",
    accessToken = "YOUR_ACCESS_TOKEN",
    sessionToken = "unique-session-token",
    proximity = "9.0,45.3",
    limit = 5,
    language = "en"
)
```
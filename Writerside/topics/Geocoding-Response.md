# Geocoding [Response]

The `GeocodingResponse` class and its related data classes represent the structure of the response returned by the geocoding API. These classes are used to parse and store data about geocoded locations, including detailed information about features, properties, geometry, and contextual metadata.

## GeocodingResponse

The `GeocodingResponse` class encapsulates the overall response from the geocoding API, including metadata, query terms, and the list of geocoded features.

| Field       | Type                     | Description                                       |
|-------------|--------------------------|---------------------------------------------------|
| type        | String                   | Type of the response (e.g., "FeatureCollection"). |
| query       | List<String()>?          | Query terms provided for geocoding (nullable).    |
| features    | List<GeocodingFeature()> | List of features returned from the API.           |
| attribution | String                   | Attribution text for the response.                |

---

## GeocodingFeature

The `GeocodingFeature` class represents an individual geocoded feature. Each feature contains information such as its type, relevance, bounding box, geometry, and additional context.

| Field      | Type                      | Description                                          |
|------------|---------------------------|------------------------------------------------------|
| id         | String                    | Unique identifier of the feature.                    |
| type       | String                    | Type of the feature (e.g., "Feature").               |
| place_type | List<String()>?           | Categories of the feature (nullable).                |
| relevance  | Double                    | Relevance score of the feature.                      |
| properties | GeocodingProperties       | Properties of the feature, such as accuracy.         |
| text       | String?                   | Short display text for the feature (nullable).       |
| place_name | String?                   | Full name of the place (nullable).                   |
| bbox       | List<Double()>?           | Bounding box for the feature (nullable).             |
| geometry   | GeocodingGeometry         | Geometry details, including coordinates.             |
| context    | List<GeocodingContext()>? | Contextual information about the feature (nullable). |

---

## GeocodingProperties

The `GeocodingProperties` class provides additional metadata and properties associated with a geocoded feature, such as its accuracy, category, and related identifiers.

| Field           | Type    | Description                                  |
|-----------------|---------|----------------------------------------------|
| accuracy        | String? | Accuracy of the geocoding result (nullable). |
| wikidata        | String? | Wikidata reference for the place (nullable). |
| short_code      | String? | Short code for the place (nullable).         |
| address         | String? | Address information (nullable).              |
| category        | String? | Category of the place (nullable).            |
| maki            | String? | Maki icon identifier (nullable).             |
| full_address    | String? | Full formatted address (nullable).           |
| place_formatted | String? | Alternate formatted place name (nullable).   |

---

## GeocodingGeometry

The `GeocodingGeometry` class defines the spatial characteristics of a geocoded feature, including its type and coordinates.

| Field       | Type           | Description                                        |
|-------------|----------------|----------------------------------------------------|
| type        | String         | Type of geometry (e.g., "Point").                  |
| coordinates | List<Double()> | Coordinates of the geometry (longitude, latitude). |

---

## GeocodingContext

The `GeocodingContext` class provides additional contextual information about a geocoded feature, such as its hierarchy or associated codes.

| Field       | Type    | Description                                          |
|-------------|---------|------------------------------------------------------|
| id          | String  | Unique identifier for the context item.              |
| text        | String  | Text description of the context.                     |
| wikidata    | String? | Wikidata reference for the context (nullable).       |
| short_code  | String? | Short code for the context (nullable).               |

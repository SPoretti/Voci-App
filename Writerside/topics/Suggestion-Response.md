# Suggestion [Response]

The `SuggestionResponse` class and its related data classes represent the structure of the response returned by the suggestions API. These classes are used to parse and store data about suggestions, including details about the name, location, context, and metadata.

## SuggestionResponse

The `SuggestionResponse` class encapsulates the overall response from the suggestions API, including a list of suggestions and attribution information.

| Field       | Type             | Description                                |
|-------------|------------------|--------------------------------------------|
| suggestions | List<Suggestion> | List of suggestions returned from the API. |
| attribution | String           | Attribution text for the response.         |

---

## Suggestion

The `Suggestion` class represents an individual suggestion. It contains various attributes such as the suggestion name, address, context, language, and metadata.

| Field            | Type                 | Description                                          |
|------------------|----------------------|------------------------------------------------------|
| name             | String               | Name of the suggestion.                              |
| mapbox_id        | String               | Unique identifier for the suggestion in Mapbox.      |
| feature_type     | String               | Type of feature for the suggestion.                  |
| address          | String?              | Address of the suggestion (nullable).                |
| full_address     | String?              | Full formatted address of the suggestion (nullable). |
| place_formatted  | String               | Alternate formatted place name for the suggestion.   |
| context          | Context              | Contextual information about the suggestion.         |
| language         | String               | Language associated with the suggestion.             |
| maki             | String?              | Maki icon identifier (nullable).                     |
| poi_category     | List<String>?        | Categories of the point of interest (nullable).      |
| poi_category_ids | List<String>?        | IDs of the point of interest categories (nullable).  |
| brand            | List<String>?        | Brands associated with the suggestion (nullable).    |
| brand_id         | List<String>?        | IDs for the brands (nullable).                       |
| external_ids     | Map<String, String>? | External identifiers for the suggestion (nullable).  |
| metadata         | Map<String, Any>?    | Additional metadata about the suggestion (nullable). |
| distance         | Double?              | Distance associated with the suggestion (nullable).  |
| eta              | Double?              | Estimated time of arrival (nullable).                |
| added_distance   | Double?              | Additional distance (nullable).                      |
| added_time       | Double?              | Additional time (nullable).                          |

---

## Context

The `Context` class provides contextual information about a suggestion, including geographical and administrative divisions such as country, region, postcode, and more.

| Field        | Type          | Description                          |
|--------------|---------------|--------------------------------------|
| country      | Country?      | Country information (nullable).      |
| region       | Region?       | Region information (nullable).       |
| postcode     | Postcode?     | Postcode information (nullable).     |
| district     | District?     | District information (nullable).     |
| place        | Place?        | Place information (nullable).        |
| locality     | Locality?     | Locality information (nullable).     |
| neighborhood | Neighborhood? | Neighborhood information (nullable). |
| address      | Address?      | Address information (nullable).      |
| street       | Street?       | Street information (nullable).       |

---

## Country

The `Country` class represents information about the country associated with a suggestion.

| Field                | Type    | Description                                   |
|----------------------|---------|-----------------------------------------------|
| id                   | String? | Unique identifier for the country (nullable). |
| name                 | String  | Name of the country.                          |
| country_code         | String  | Country code for the country.                 |
| country_code_alpha_3 | String  | 3-letter country code (ISO 3166-1 alpha-3).   |

---

## Region

The `Region` class represents information about the region associated with a suggestion.

| Field                | Type    | Description                                              |
|----------------------|---------|----------------------------------------------------------|
| id                   | String? | Unique identifier for the region (nullable).             |
| name                 | String  | Name of the region.                                      |
| region_code          | String  | Region code.                                             |
| region_code_full     | String  | Full region code.                                        |

---

## Postcode

The `Postcode` class represents information about the postcode associated with a suggestion.

| Field | Type    | Description                                    |
|-------|---------|------------------------------------------------|
| id    | String? | Unique identifier for the postcode (nullable). |
| name  | String  | Name of the postcode.                          |

---

## District

The `District` class represents information about the district associated with a suggestion.

| Field | Type    | Description                                    |
|-------|---------|------------------------------------------------|
| id    | String? | Unique identifier for the district (nullable). |
| name  | String  | Name of the district.                          |

---

## Place

The `Place` class represents information about a place associated with a suggestion.

| Field | Type    | Description                                 |
|-------|---------|---------------------------------------------|
| id    | String? | Unique identifier for the place (nullable). |
| name  | String  | Name of the place.                          |

---

## Locality

The `Locality` class represents information about the locality associated with a suggestion.

| Field | Type    | Description                                    |
|-------|---------|------------------------------------------------|
| id    | String? | Unique identifier for the locality (nullable). |
| name  | String  | Name of the locality.                          |

---

## Neighborhood

The `Neighborhood` class represents information about the neighborhood associated with a suggestion.

| Field | Type    | Description                                        |
|-------|---------|----------------------------------------------------|
| id    | String? | Unique identifier for the neighborhood (nullable). |
| name  | String  | Name of the neighborhood.                          |

---

## Address

The `Address` class represents information about the address associated with a suggestion.

| Field          | Type    | Description                                   |
|----------------|---------|-----------------------------------------------|
| id             | String? | Unique identifier for the address (nullable). |
| name           | String  | Name of the address.                          |
| address_number | String? | Address number (nullable).                    |
| street_name    | String? | Street name (nullable).                       |

---

## Street

The `Street` class represents information about the street associated with a suggestion.

| Field | Type    | Description                                  |
|-------|---------|----------------------------------------------|
| id    | String? | Unique identifier for the street (nullable). |
| name  | String  | Name of the street.                          |

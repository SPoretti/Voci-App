package com.example.vociapp.data.api

data class GeocodingResponse(
    val type: String,
    val query: List<String>?, // Make query nullable
    val features: List<GeocodingFeature>,
    val attribution: String
)

data class GeocodingFeature(
    val id: String,
    val type: String,
    val place_type: List<String>?, // Make place_type nullable
    val relevance: Double,
    val properties: GeocodingProperties,
    val text: String?, // Make text nullable
    val place_name: String?, // Make place_name nullable
    val bbox: List<Double>?,
    val geometry: GeocodingGeometry, // Use geometry object
    val context: List<GeocodingContext>? // Make context nullable
)

data class GeocodingProperties(
    val accuracy: String?,
    val wikidata: String?,
    val short_code: String?,
    val address: String?,
    val category: String?,
    val maki: String?,
    val full_address: String?, // Add full_address
    val place_formatted: String? // Add place_formatted
)

data class GeocodingGeometry(
    val type: String,
    val coordinates: List<Double>
)

data class GeocodingContext(
    val id: String,
    val text: String,
    val wikidata: String?,
    val short_code: String?
)
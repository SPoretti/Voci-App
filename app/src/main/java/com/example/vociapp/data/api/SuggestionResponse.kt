package com.example.vociapp.data.api

data class SuggestionResponse(
    val suggestions: List<Suggestion>,
    val attribution: String
)

data class Suggestion(
    val name: String,
    val mapbox_id: String,
    val feature_type: String,
    val address: String?,
    val full_address: String?,
    val place_formatted: String,
    val context: Context,
    val language: String,
    val maki: String?,
    val poi_category: List<String>?,
    val poi_category_ids: List<String>?,
    val brand: List<String>?,
    val brand_id: List<String>?,
    val external_ids: Map<String, String>?,
    val metadata: Map<String, Any>?,
    val distance: Double?,
    val eta: Double?,
    val added_distance: Double?,
    val added_time: Double?
)

data class Context(
    val country: Country?,
    val region: Region?,
    val postcode: Postcode?,
    val district: District?,
    val place: Place?,
    val locality: Locality?,
    val neighborhood: Neighborhood?,
    val address: Address?,
    val street: Street?
)

data class Country(
    val id: String?,
    val name: String,
    val country_code: String,
    val country_code_alpha_3: String
)

data class Region(
    val id: String?,
    val name: String,
    val region_code: String,
    val region_code_full: String
)

data class Postcode(
    val id: String?,
    val name: String
)

data class District(
    val id: String?,
    val name: String
)

data class Place(
    val id: String?,
    val name: String
)

data class Locality(
    val id: String?,
    val name: String
)

data class Neighborhood(
    val id: String?,
    val name: String
)

data class Address(
    val id: String?,
    val name: String,
    val address_number: String?,
    val street_name: String?
)

data class Street(
    val id: String?,
    val name: String
)
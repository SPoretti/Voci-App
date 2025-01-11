package com.example.vociapp.data.remote

import com.example.vociapp.data.remote.RetrofitClient.geocodingApi
import com.example.vociapp.data.util.GeocodingResponse

class MapboxGeocodingClient () {
    suspend fun geocodeAddress(
        query: String,
        accessToken: String,
        language: String? = null,
        proximity: String? = null,
        country: String? = null,
        types: String? = null,
        limit: Int? = null,
        autocomplete: Boolean? = null,
        bbox: String? = null,
        worldview: String? = null,
        format: String? = null,
        permanent: Boolean? = null
    ): GeocodingResponse {
        return geocodingApi.geocodeAddress(
            query = query,
            accessToken = accessToken,
            language = language,
            proximity = proximity,
            country = country,
            types = types,
            limit = limit,
            autocomplete = autocomplete,
            bbox = bbox,
            worldview = worldview,
            format = format,
            permanent = permanent
        )
    }

    suspend fun reverseGeocode(
        longitude: String,
        latitude: String,
        accessToken: String,
        language: String? = null,
        types: String? = null,
        limit: Int? = null,
        worldview: String? = null,
        permanent: Boolean? = null
    ): GeocodingResponse {
        return geocodingApi.reverseGeocode(
            longitude = longitude,
            latitude = latitude,
            accessToken = accessToken,
            language = language,
            types = types,
            limit = limit,
            worldview = worldview,
            permanent = permanent
        )
    }
}
package com.voci.app.data.repository

import com.voci.app.data.api.GeocodingResponse
import com.voci.app.data.api.SuggestionResponse
import com.voci.app.data.remote.RetrofitFactory

class MapboxRepository {
    private val searchApi = RetrofitFactory.searchApi
    private val geocodingApi = RetrofitFactory.geocodingApi

    suspend fun getMapboxSuggestions(
        query: String,
        accessToken: String,
        sessionToken: String,
        language: String? = null,
        limit: Int? = null,
        proximity: String? = null,
        bbox: String? = null,
        country: String? = null,
        types: String? = null,
        poiCategory: String? = null,
        poiCategoryExclusions: String? = null,
        etaType: String? = null,
        navigationProfile: String? = null,
        origin: String? = null
    ): SuggestionResponse {
        return searchApi.getSuggestions(
            query = query,
            accessToken = accessToken,
            sessionToken = sessionToken,
            language = language,
            limit = limit,
            proximity = proximity,
            bbox = bbox,
            country = country,
            types = types,
            poiCategory = poiCategory,
            poiCategoryExclusions = poiCategoryExclusions,
            etaType = etaType,
            navigationProfile = navigationProfile,
            origin = origin
        )
    }

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
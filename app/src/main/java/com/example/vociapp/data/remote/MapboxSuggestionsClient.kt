package com.example.vociapp.data.remote

import com.example.vociapp.data.util.SuggestionResponse

class MapboxSuggestionsClient (){
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
        return RetrofitClient.api.getSuggestions(
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
}
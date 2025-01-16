package com.voci.app.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchInterface {
    @GET("search/searchbox/v1/suggest")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("access_token") accessToken: String,
        @Query("session_token") sessionToken: String,
        @Query("language") language: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("proximity") proximity: String? = null,
        @Query("bbox") bbox: String? = null,
        @Query("country") country: String? = null,
        @Query("types") types: String? = null,
        @Query("poi_category") poiCategory: String? = null,
        @Query("poi_category_exclusions") poiCategoryExclusions: String? = null,
        @Query("eta_type") etaType: String? = null,
        @Query("navigation_profile") navigationProfile: String? = null,
        @Query("origin") origin: String? = null
    ): SuggestionResponse
}
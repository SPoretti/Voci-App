package com.example.vociapp.data.util

import retrofit2.http.GET
import retrofit2.http.Query

interface MapboxGeocodingApi {
    @GET("search/geocode/v6/forward")
    suspend fun geocodeAddress(
        @Query("q") query: String,
        @Query("access_token") accessToken: String,
        @Query("language") language: String? = null,
        @Query("proximity") proximity: String? = null,
        @Query("country") country: String? = null,
        @Query("types") types: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("autocomplete") autocomplete: Boolean? = null,
        @Query("bbox") bbox: String? = null,
        @Query("worldview") worldview: String? = null,
        @Query("format") format: String? = null,
        @Query("permanent") permanent: Boolean? = null
    ): GeocodingResponse

    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: String,
        @Query("latitude") latitude: String,
        @Query("access_token") accessToken: String,
        @Query("language") language: String? = null,
        @Query("types") types: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("worldview") worldview: String? = null,
        @Query("permanent") permanent: Boolean? = null
    ): GeocodingResponse
}